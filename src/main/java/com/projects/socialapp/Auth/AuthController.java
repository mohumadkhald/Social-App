package com.projects.socialapp.Auth;

import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    /*
    |--------------------------------------------------------------------------
    | Inject The Service Auth
    |--------------------------------------------------------------------------
    |
    */
    private final AuthService authService;


    /*
    |--------------------------------------------------------------------------
    | API Routes Register
    |--------------------------------------------------------------------------
    |
    | Here is where you can register API routes for your application. These
    | routes are loaded by the RouteServiceProvider and all of them will
    | be assigned to the "api" middleware group. Make something great!
    | after register you will receive token
    |
    */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequestDto request)
    {
        return ResponseEntity.ok((authService.register(request)));
    }

     /*
    |--------------------------------------------------------------------------
    | API Routes Login
    |--------------------------------------------------------------------------
    |
    | Here is where you can Log in API routes for your application.
    | After login take token to browse in website as you need
    |
    */

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }


}
