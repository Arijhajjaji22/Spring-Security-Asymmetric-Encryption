package com.arij.project.validation;

import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


import org.springframework.beans.factory.annotation.Value;

public class EmailDomainValidator implements ConstraintValidator<NonDisposableEmail, String> {

   // private static final String DISPOSABLE_EMAIL_DOMAINS = "mailinator.com,10minutemail.com,temp-mail.org";

   private final Set<String> blocked ;
   
   public EmailDomainValidator(

         @Value("${app.security.disposable-email}") 
         final List<String> domains
   
) {
    this.blocked=domains.stream()
                         .map(String::toLowerCase)
                         .collect(Collectors.toSet());
       
    }

    @Override
    public boolean isValid( final String email, final ConstraintValidatorContext context) {
        if (email == null || email.isEmpty()) {
            return true;
      
        }
        final int atIndex = email.lastIndexOf('@');
        final int dotIndex = email.lastIndexOf('.');
        final String domain = email.substring(atIndex + 1, dotIndex);
        return !this.blocked.contains(domain);
    }
}