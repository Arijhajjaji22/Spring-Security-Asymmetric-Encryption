package com.arij.project.auth.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    @NotBlank(message="VALIDATION.AUTHENTICATION.EMAIL.NOT_BLANK") 
    @Email(message="VALIDATION.AUTHENTICATION.EMAIL.FORMAT")
    @Schema(example = "arij@mail.com")
    private String email ;
    @NotBlank(message="VALIDATION.AUTHENTICATION.PASSWORD.NOT_BLANK")
    @Schema(example = "password")
    private String password;
    private boolean rememberMe;
    
}
