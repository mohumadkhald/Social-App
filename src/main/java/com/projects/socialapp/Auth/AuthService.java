package com.projects.socialapp.Auth;

import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.UserRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public User findUserProfileByJwt(String jwt);

    Integer findUserIdByJwt(String jwt);

    ResponseEntity<?> createUserHandeler(UserRequestDto user);

    ResponseEntity<?> login(LoginRequestDto loginRequest);
}
