package com.arij.project.auth;


import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arij.Handler.ErrorResponse;
import com.arij.project.auth.request.AuthenticationRequest;
import com.arij.project.auth.request.GoogleAuthRequest;
import com.arij.project.auth.request.RefreshRequest;
import com.arij.project.auth.request.RegistrationRequest;
import com.arij.project.auth.response.AuthenticationResponse;
import com.arij.project.exception.BusinessException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name="Authentication", description="Authenication API")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
public ResponseEntity<AuthenticationResponse> login(
    @Valid
    @RequestBody AuthenticationRequest request
) {
    // Appelle le service qui authentifie et renvoie le token
    AuthenticationResponse response = this.authenticationService.login(request);

    // Si l'utilisateur a coché "Remember Me"
    if (request.isRememberMe()) {
        // Modifie l'expiration du token, ici 30 jours
        response.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
    }

    // Retourne la réponse
    return ResponseEntity.ok(response);
}

@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
    try {
        authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (BusinessException ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            ex.getErrorCode().getCode(),
            null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(error);
    }
}

@PostMapping("/google")
public ResponseEntity<AuthenticationResponse> loginWithGoogle(
    @Valid
    @RequestBody
    GoogleAuthRequest request
) {
    return ResponseEntity.ok(this.authenticationService.loginWithGoogle(request));
}




    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(
        
        @RequestBody
        final RefreshRequest request
    ) {
        return ResponseEntity.ok(this.authenticationService.refreshToken(request));
    }
    
}
