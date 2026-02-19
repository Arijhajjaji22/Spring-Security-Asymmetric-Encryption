package com.arij.project.auth.impl;

import com.arij.project.user.PasswordResetToken;
import com.arij.project.user.PasswordResetTokenRepository;
import com.arij.project.user.User;
import com.arij.project.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public void sendForgotPasswordEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = createPasswordResetToken(email);

        // Création du message email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset your password");
        message.setText("Click this link to reset your password:\n" +
                "http://localhost:4200/reset-password?token=" + token +
                "\n\nThis link expires in 1 hour.");

        // Envoi
        mailSender.send(message);
    }

    public String createPasswordResetToken(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
        tokenRepository.save(resetToken);
        return token;
    }

   public void resetPassword(String token, String newPassword) {
    PasswordResetToken resetToken = tokenRepository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));

    User user;
    try {
        user = resetToken.getUser(); 
    } catch (EntityNotFoundException e) {
        tokenRepository.delete(resetToken); // supprime le token orphelin
        throw new RuntimeException("Associated user not found. Token invalidated.");
    }

    if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
        throw new RuntimeException("Token expired");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    tokenRepository.delete(resetToken);
}

}
