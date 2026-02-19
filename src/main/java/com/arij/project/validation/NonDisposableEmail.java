package com.arij.project.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;



/**
 * Indique où l'annotation @NonDisposableEmail peut être utilisée.
 * 
 * FIELD      → sur un attribut (ex: private String email)
 * METHOD     → sur une méthode
 * PARAMETER  → sur un paramètre de méthode
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailDomainValidator.class)
public @interface NonDisposableEmail {
    String message() default "Disposable email addresses are not allowed.";
    Class<?>[] groups()default {};
    Class<? extends Payload>[] payload() default {};


}
