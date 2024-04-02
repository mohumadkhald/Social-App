package com.projects.socialapp.Auth;

import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import com.projects.socialapp.service.UserService;
import com.projects.socialapp.token.TokenRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final UserService userService;
    private final TokenRepo tokenRepo;


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
    public AuthResponse login(@Valid @RequestBody LoginRequestDto request) throws Exception {
        return authService.login(request);
    }



    @PostMapping("/logout/all")
    public void logoutAll(@RequestHeader("Authorization") String jwtToken) {
        String token = jwtToken.substring(7); // Extract token from "Bearer TOKEN"
        Integer userId = userService.findUserIdByJwt(jwtToken);
            // Retrieve the tokens from the repository
        var validTokensForUser = tokenRepo.findAllValidTokenByUser(userId);
        if (validTokensForUser.isEmpty())
            return;
        validTokensForUser.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepo.saveAll(validTokensForUser);
    }






}
