package com.arij.project.auth.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.arij.project.validation.NonDisposableEmail;

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
public class RegistrationRequest {

    @NotBlank(message="VALIDATION.REGISTRATION.FIRSTNAME.NOT_BLANK")
    @Size(min = 1, max = 50, message = "VALIDATION.REGISTRATION.FIRSTNAME.SIZE")
    @Pattern(
        regexp = "^[\\p{L}'-]+$", 
        message = "VALIDATION.REGISTRATION.FIRSTNAME.PATTERN"
    )
    @Schema(example = "Arij")
    private String firstName;
      @NotBlank(message="VALIDATION.REGISTRATION.LASTNAME.NOT_BLANK")
    @Size(min = 1, max = 50, message = "VALIDATION.REGISTRATION.LASTNAME.SIZE")
    @Pattern(
        regexp = "^[\\p{L}'-]+$", 
        message = "VALIDATION.REGISTRATION.LASTNAME.PATTERN"
    )
    @Schema(example = "Arij")
    private String lastName ;
    @NotBlank(message="VALIDATION.REGISTRATION.EMAIL.NOT_BLANK")
    @Email(message="VALIDATION.REGISTRATION.EMAIL.FORMAT")
    @NonDisposableEmail(message="VALIDATION.REGISTRATION.EMAIL.DISPOSABLE")
    @Schema(example = "arij@example.com")
    private String email ;
    @NotBlank(message="VALIDATION.REGISTRATION.PHONE_NUMBER.NOT_BLANK")
    @Pattern(
        regexp = "^\\+?[0-9]{7,15}$", 
        message = "VALIDATION.REGISTRATION.PHONE_NUMBER.FORMAT"
    )
    @Schema(example = "+1234567890")
    private String phoneNumber;
    @NotBlank(message="VALIDATION.REGISTRATION.PASSWORD.NOT_BLANK")
    @Size(min = 8,max = 72, message = "VALIDATION.REGISTRATION.PASSWORD.SIZE")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).+$", 
        message = "VALIDATION.REGISTRATION.PASSWORD.WEAK"
    )
    @Schema(example = "<password>")
    private String password;
        @NotBlank(message="VALIDATION.REGISTRATION.CONFIRM_PASSWORD.NOT_BLANK") 
        @Size(min = 8,max = 72, message = "VALIDATION.REGISTRATION.CONFIRM_PASSWORD.SIZE")
   @Schema(example = "<Password>")
    private String confirmPassword;
   
  
    
}
