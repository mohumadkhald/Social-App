package com.projects.socialapp.Auth;

import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.RegisterRequestDto;

public interface AuthService {

    // Method Register User to Website
    AuthResponse register(RegisterRequestDto request);

    //Method Log in For Website
    AuthResponse login(LoginRequestDto request);
}