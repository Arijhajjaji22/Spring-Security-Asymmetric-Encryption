package com.arij.project.user.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.arij.project.exception.BusinessException;
import com.arij.project.exception.ErrorCode;
import com.arij.project.user.User;
import com.arij.project.user.UserMapper;
import com.arij.project.user.UserRepository;
import com.arij.project.user.UserService;
import com.arij.project.user.request.ChangePasswordRequest;
import com.arij.project.user.request.ProfileUpdateRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final UserMapper userMapper ;
    @Override
    public UserDetails loadUserByUsername(final String userEmail) throws UsernameNotFoundException {
        return this.userRepository.findByEmailIgnoreCase(userEmail)
           .orElseThrow(() -> new UsernameNotFoundException("User not found with username :"));
    }
    @Override
    public void updateProfileInfo(ProfileUpdateRequest request, String userId) {
       final User savedUser = this .userRepository.findById(userId)
       .orElseThrow(()->new BusinessException(ErrorCode.USER_NOT_FOUND ,userId));
    this.userMapper.mergerUserInfo(savedUser , request );
    this.userRepository.save(savedUser);
    }
    @Override
    public void changePassword(final ChangePasswordRequest request, final String userId) {
       
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())){
            throw new BusinessException(ErrorCode.CHANGE_PASSWORD_MISMATCH);
        }
        final User savedUser = this.userRepository.findById(userId)
           .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND,userId));

        if (!this.passwordEncoder.matches(request.getCurrentPassword(), 
                                          savedUser.getPassword())){
        throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
                                        
       }
        final String encoded = this.passwordEncoder.encode(request.getNewPassword());
           savedUser.setPassword(encoded);
           this.userRepository.save(savedUser);
    }
    @Override
    public void desactivateAcount(String userId) {
        final User user = this.userRepository.findById(userId)
                                            .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND,userId));
        if (!user.isEnabled()){
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED);
        }
        user.setEnabled(false);
        this.userRepository.save(user);
    }
    @Override
    public void reactivateAccount(String userId) {
          final User user = this.userRepository.findById(userId)
                                            .orElseThrow(()-> new BusinessException(ErrorCode.USER_NOT_FOUND,userId));
        if (!user.isEnabled()){
            throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED);
        }
        user.setEnabled(true );
        this.userRepository.save(user);
      
    }
    @Override
    public void deleteAccount(String userId) {
       
    }
    
}
