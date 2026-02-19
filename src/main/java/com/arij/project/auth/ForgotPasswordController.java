package com.arij.project.auth;

import com.arij.project.auth.request.ForgotPasswordRequest;
import com.arij.project.auth.request.ResetPasswordRequest;
import com.arij.project.auth.impl.ForgotPasswordService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService service;

  @PostMapping("/forgot-password")
public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request){
    service.sendForgotPasswordEmail(request.getEmail());
    Map<String, String> response = new HashMap<>();
    response.put("message", "Check your email! If it exists, you'll receive a reset link.");
    return ResponseEntity.ok(response);
}



  @PostMapping("/reset-password")
public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest request){
    service.resetPassword(request.getToken(), request.getNewPassword());
    Map<String, String> response = new HashMap<>();
    response.put("message", "Password reset successful");
    return ResponseEntity.ok(response);
}

}
