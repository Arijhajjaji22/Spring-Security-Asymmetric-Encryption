package com.arij.project.user;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Service;

import com.arij.project.auth.request.RegistrationRequest;
import com.arij.project.user.request.ProfileUpdateRequest;

@Service
public class UserMapper {

    public void mergerUserInfo(final User user, ProfileUpdateRequest request) {
       if (StringUtils.isNotBlank(request.getFirstName())
        && !user.getFirstName().equals(request.getFirstName())){
          user.setFirstName((request.getFirstName()));
       }
         if (StringUtils.isNotBlank(request.getLastName())
        && !user.getLastName().equals(request.getLastName())){
          user.setLastName((request.getLastName()));
       }
       
         if (request.getDateOfBirth()!= null
             && !user.getFirstName().equals(request.getFirstName())){
          user.setDateOfBirth((request.getDateOfBirth()));
       }
       
    }

    public User toUser(RegistrationRequest request) {
      return User.builder()
                 .firstName(request.getFirstName())
                 .lastName(request.getLastName())
                 .email(request.getEmail())
                 .phoneNumber(request.getPhoneNumber())
                 .password(request.getPhoneNumber())
                 .enabled(true)
                 .locked(false)
                 .credentialsExpired(false)
                 .emailVerified(false)
                 .phoneVerified(false)
                 .build();
    }

}
