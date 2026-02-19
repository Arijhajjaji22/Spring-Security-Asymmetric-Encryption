package com.arij.Handler;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class ErrorResponse {
    private String message ;
    private String code ;
    private List<ValidationError> validationErrors ;

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ValidationError {
        private String field ;
        private String code ;
        private String message ;
    }


}
