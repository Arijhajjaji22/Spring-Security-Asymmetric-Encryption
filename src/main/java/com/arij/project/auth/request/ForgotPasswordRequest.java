// ForgotPasswordRequest.java
package com.arij.project.auth.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {
    private String email;
}
