package com.projects.socialapp.Auth;

import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.UserRequestDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<?> createUserHandler(@Valid @RequestBody UserRequestDto user) {
            return authService.createUserHandeler(user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequest) {

        return authService.login(loginRequest);
    }
}
