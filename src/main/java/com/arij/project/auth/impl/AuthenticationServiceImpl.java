package com.arij.project.auth.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.arij.project.Security.JwtService;
import com.arij.project.auth.AuthenticationService;
import com.arij.project.auth.request.AuthenticationRequest;
import com.arij.project.auth.request.GoogleAuthRequest;
import com.arij.project.auth.request.RefreshRequest;
import com.arij.project.auth.request.RegistrationRequest;
import com.arij.project.auth.response.AuthenticationResponse;
import com.arij.project.exception.BusinessException;
import com.arij.project.exception.ErrorCode;
import com.arij.project.role.Role;
import com.arij.project.role.RoleRepository;
import com.arij.project.user.AuthProvider;
import com.arij.project.user.User;
import com.arij.project.user.UserMapper;
import com.arij.project.user.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{


    @Value("${google.client.id}")
    private String googleClientId;

    private final AuthenticationManager authenticationManager ;
    private final JwtService jwtService ;
    private final UserRepository userRepository ;
    private final RoleRepository roleRepository ;
    private final UserMapper userMapper ;
    private final PasswordEncoder passwordEncoder ;
    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
       final Authentication auth = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
       );
       final User user = (User) auth.getPrincipal();
       final String token = this.jwtService.generateAccessToken(user.getUsername());
       final String refreshtoken = this.jwtService.generateRefreshToken(user.getUsername());
       final String tokenType = "Bearer" ;
       return AuthenticationResponse.builder()
                                    .accessToken(token)
                                    .refreshToken(refreshtoken)
                                    .tokenType(tokenType)
                                    .build();
      
    }

    @Override
    @Transactional
    public void register(final RegistrationRequest request) {
        checkUserEmail(request.getEmail());
        checkUserPhoneNumber(request.getPhoneNumber());
        checkPasswords(request.getPassword(),request.getConfirmPassword());

      // ...existing code...
        final Role userRole = this.roleRepository.findByName("ROLE_USER")
            .orElseGet(() -> {
                Role r = new Role();
                r.setName("ROLE_USER");
                r.setCreatedBy("system");
                r.setCreatedDate(LocalDateTime.now()); 
                return this.roleRepository.save(r);
            });

       
        final List<Role> roles = new ArrayList<> () ;
        roles.add(userRole);

          final User user = this.userMapper.toUser(request);
        // encode password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        log.debug("Saving user {}",user);
        this.userRepository.save(user);

       // final List<User> users = new ArrayList<>();
        //users.add(user);
        //userRole.setUsers(users);
       // this.roleRepository.save(userRole);


    }

    @Override
    public AuthenticationResponse refreshToken(final RefreshRequest request) {
        
        final String newAccessToken = this.jwtService.refreshAccessToken(request.getRefreshToken());
        final String tokenType = "Bearer" ;
        return AuthenticationResponse.builder()
                                     .accessToken(newAccessToken)
                                     .refreshToken(request.getRefreshToken())
                                     .tokenType(tokenType)
                                     .build();
      }

   private void checkUserEmail(final String email) {
    if (this.userRepository.existsByEmailIgnoreCase(email)) {
        // Passer l'ErrorCode pour que le handler sache que c'est une erreur liée au champ
        throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email already exists");
    }
}

private void checkUserPhoneNumber(final String phoneNumber) {
    if (this.userRepository.existsByPhoneNumber(phoneNumber)) {
        throw new BusinessException(ErrorCode.PHONE_ALREADY_EXISTS, "Phone number already exists");
    }
}

private void checkPasswords(final String password, final String confirmPassword) {
    if (password == null || !password.equals(confirmPassword)) {
        throw new BusinessException(ErrorCode.PASSWORD_MISMATCH, "Passwords do not match");
    }
}

@Override
@Transactional
public AuthenticationResponse loginWithGoogle(GoogleAuthRequest request) {
    try {
        // Vérifier le token Google
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance()
        ).setAudience(Collections.singletonList(googleClientId)).build();

        GoogleIdToken idToken = verifier.verify(request.getIdToken());
        if (idToken == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN, "Invalid Google ID token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String givenName = (String) payload.get("given_name");
        String familyName = (String) payload.get("family_name");

        // Vérifier si l'utilisateur existe
     // Vérifier si l'utilisateur existe
User user = userRepository.findByEmailIgnoreCase(email).orElseGet(() -> {
    // Créer un nouvel utilisateur
    User newUser = new User();
    newUser.setEmail(email);
    newUser.setFirstName(givenName != null ? givenName : name);
    newUser.setLastName(familyName != null ? familyName : "");
    newUser.setPhoneNumber(UUID.randomUUID().toString());
    // 🔑 Mot de passe temporaire obligatoire pour la DB
    String randomPassword = UUID.randomUUID().toString(); 
    newUser.setPassword(passwordEncoder.encode(randomPassword));
    
    newUser.setProvider(AuthProvider.GOOGLE);
    newUser.setEnabled(true); // Important!
    newUser.setCreatedDate(LocalDateTime.now());
    

    // Ajouter le rôle USER
    Role userRole = roleRepository.findByName("ROLE_USER")
        .orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            r.setCreatedBy("system");
            r.setCreatedDate(LocalDateTime.now());
            return roleRepository.save(r);
        });

    newUser.setRoles(Collections.singletonList(userRole));

    return userRepository.save(newUser);
});

        // Générer JWT
        String jwt = jwtService.generateAccessToken(user.getEmail()); // Utiliser email au lieu de getUsername()
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();

    } catch (Exception e) {
        log.error("Google login failed", e);
        throw new BusinessException(ErrorCode.AUTHENTICATION_FAILED, "Google login failed: " + e.getMessage());
    }   
}
}