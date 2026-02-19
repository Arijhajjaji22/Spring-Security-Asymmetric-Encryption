package com.arij.project.auth;

import com.arij.project.auth.request.AuthenticationRequest;
import com.arij.project.auth.request.GoogleAuthRequest;
import com.arij.project.auth.request.RefreshRequest;
import com.arij.project.auth.request.RegistrationRequest;
import com.arij.project.auth.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request);

    void register (RegistrationRequest request);

    AuthenticationResponse refreshToken(RefreshRequest request);
     AuthenticationResponse loginWithGoogle(GoogleAuthRequest request);
    

    

    

    
}
