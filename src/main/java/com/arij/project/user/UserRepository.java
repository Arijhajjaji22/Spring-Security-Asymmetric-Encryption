package com.arij.project.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <User,String>{

    //TEST@MAIL.COM = test@mail.com
    // Ali@mial.com == ali@mail.com
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmailIgnoreCase(String email);
    
    boolean existsByPhoneNumber(String email);


}
