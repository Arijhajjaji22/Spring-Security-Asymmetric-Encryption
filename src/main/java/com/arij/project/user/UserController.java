package com.arij.project.user;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.arij.project.user.request.ChangePasswordRequest;
import com.arij.project.user.request.ProfileUpdateRequest;

import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name="User" ,description = "User API")
public class UserController {

    private final UserService userService ;

    @PatchMapping("/me")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void updateProfileInfo(
        @RequestBody
        @Valid
        final ProfileUpdateRequest request,
        final Authentication principal
    ) {
        this.userService.updateProfileInfo(request, getUserId(principal));

    }
    
    @PostMapping("/me/password")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void changePassword(
        @RequestBody
        @Valid
        final ChangePasswordRequest request ,
        final Authentication principal 
    ){
        this.userService.changePassword(request, getUserId(principal));
    }

    @PatchMapping("/me/deactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void desactivateAcount (final Authentication principal){
        this.userService.desactivateAcount(getUserId(principal));
    }

    @PatchMapping("/me/reactivate")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void resactivateAcount (final Authentication principal){
        this.userService.desactivateAcount(getUserId(principal));
    }

    @DeleteMapping("/me")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAccount(final Authentication principal){
        this.userService.deleteAccount(getUserId(principal));
    }

    private String getUserId(final Authentication principal) {
    return ((User) principal.getPrincipal()).getId().toString();
}

    
}
