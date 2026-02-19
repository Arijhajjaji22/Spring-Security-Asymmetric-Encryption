package com.arij.project.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.arij.project.user.request.ChangePasswordRequest;
import com.arij.project.user.request.ProfileUpdateRequest;

public interface UserService extends UserDetailsService {

    void updateProfileInfo(ProfileUpdateRequest request, String userId);

    void changePassword(ChangePasswordRequest request ,String userId);

    void desactivateAcount(String userId);

    void reactivateAccount(String userId);
    
    void deleteAccount(String userId);
}
