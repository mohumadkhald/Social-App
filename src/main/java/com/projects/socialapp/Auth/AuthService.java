package com.projects.socialapp.Auth;

import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    // Method Register User to Website
    AuthResponse register(RegisterRequestDto request);

    ResponseEntity<String> resendVerificationEmail(String email) throws Exception;

    //Method Log in For Website
    AuthResponse login(LoginRequestDto request) throws Exception;
}