package com.arij.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.arij.project.exception.BusinessException;
import com.arij.project.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApplicationExceptionHandler {

   @ExceptionHandler(BusinessException.class)
public ResponseEntity<ErrorResponse> handleException(final BusinessException ex) {
    log.error("Business exception occurred", ex);

    ErrorResponse.ValidationError fieldError = null;

    // 🔥 Mapping dynamique des champs
    switch (ex.getErrorCode()) {
        case EMAIL_ALREADY_EXISTS:
            fieldError = ErrorResponse.ValidationError.builder()
                    .field("email")
                    .code(ex.getErrorCode().getCode())
                    .message(ex.getMessage())
                    .build();
            break;
        case PHONE_ALREADY_EXISTS:
            fieldError = ErrorResponse.ValidationError.builder()
                    .field("phoneNumber")
                    .code(ex.getErrorCode().getCode())
                    .message(ex.getMessage())
                    .build();
            break;
        case PASSWORD_MISMATCH:
            fieldError = ErrorResponse.ValidationError.builder()
                    .field("password")
                    .code(ex.getErrorCode().getCode())
                    .message(ex.getMessage())
                    .build();
            break;
               default:
        // 🔥 IMPORTANT → évite le warning + fallback global
        break;
    }

    if (fieldError != null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .code(ex.getErrorCode().getCode())
                        .message(ex.getMessage())
                        .validationErrors(Collections.singletonList(fieldError))
                        .build()
        );
    }

    // fallback global
    return ResponseEntity.status(
            ex.getErrorCode().getStatus() != null ? ex.getErrorCode().getStatus() : HttpStatus.BAD_REQUEST
    ).body(
            ErrorResponse.builder()
                    .code(ex.getErrorCode().getCode())
                    .message(ex.getMessage())
                    .build()
    );
}

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(final BadCredentialsException exp) {
        log.debug(exp.getMessage(), exp);
        return ResponseEntity.status(ErrorCode.BAD_CREDENTIALS.getStatus()).body(
                ErrorResponse.builder()
                        .message(ErrorCode.BAD_CREDENTIALS.getDefaultMessage())
                        .code(ErrorCode.BAD_CREDENTIALS.getCode())
                        .build()
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(final UsernameNotFoundException exp) {
        log.debug(exp.getMessage(), exp);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .message(ErrorCode.USERNAME_NOT_FOUND.getDefaultMessage())
                        .code(ErrorCode.USERNAME_NOT_FOUND.getCode())
                        .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(EntityNotFoundException exp) {
        log.debug(exp.getMessage(), exp);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse.builder()
                        .code("TBD")
                        .message(exp.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
        log.error("Validation error occurred", ex);

        // Check for PASSWORD_MISMATCH global error first
        boolean hasPasswordMismatch = ex.getBindingResult().getGlobalErrors().stream()
                .anyMatch(err -> "PASSWORD_MISMATCH".equals(err.getDefaultMessage()));

        if (hasPasswordMismatch) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ErrorResponse.builder()
                            .code("PASSWORD_MISMATCH")
                            .message("Passwords do not match")
                            .build()
            );
        }

        List<ErrorResponse.ValidationError> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorCode = error.getDefaultMessage();
            errors.add(ErrorResponse.ValidationError.builder()
                    .field(fieldName)
                    .code(errorCode)
                    .message(errorCode)
                    .build());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponse.builder()
                        .validationErrors(errors)
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception ex) {
        log.error("Unexpected exception occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .code(ErrorCode.INTERNAL_EXCEPTION.getCode())
                        .message(ErrorCode.INTERNAL_EXCEPTION.getDefaultMessage())
                        .build()
        );
    }
}
