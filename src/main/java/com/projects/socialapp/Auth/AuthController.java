package com.projects.socialapp.Auth;

import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import com.projects.socialapp.service.UserService;
import com.projects.socialapp.token.TokenRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    private final UserRepo userRepo;


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




    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token, @RequestHeader ("Authorization") String jwt) throws Exception {
        int userID = userService.findUserIdByJwt(jwt);
        User userJwt = userService.findById(userID);
        // Find user by verification token
        User user = userRepo.findByVerificationToken(token);
        if (userJwt == user)
        {
            if (user != null && user.getVerificationTokenExpiry().isAfter(LocalDateTime.now())) {
                // Mark user's email as verified
                user.setEmailVerified(true);
                user.setVerificationToken(null);
                user.setVerificationTokenExpiry(null);
                userRepo.save(user);

                return ResponseEntity.ok("Email verification successful.");
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired token.");
            }
        } else {
            throw new Exception("invalid user");
        }

    }

    @PostMapping("resend-verify")
    public ResponseEntity<String> sendEmailVerify(@RequestHeader ("Authorization") String token) throws Exception {
        int userID = userService.findUserIdByJwt(token);
        User user = userService.findById(userID);
        return authService.resendVerificationEmail(user.getEmail());
    }



}
