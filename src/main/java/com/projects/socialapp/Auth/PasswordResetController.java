package com.projects.socialapp.Auth;

import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.model.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class PasswordResetController {
    private UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;



    @PostMapping("/send-reset")
    public ResponseEntity<String> requestResetPassword(@RequestParam("email") String email) {
        // Generate a unique token (UUID)
        // Generate a UUID
        String resetCode = UUID.randomUUID().toString();

        // Remove hyphens from UUID string
        resetCode = resetCode.replace("-", "");

        // Extract first 6 characters
        String code6 = resetCode.substring(0, 6);

        System.out.println("6-character code: " + code6);
        // Set the token and expiry time for the user in the database
        User user = userRepo.findByEmail(email);
        if (user != null) {
            user.setResetCode(code6);
            user.setResetCodeExpiry(LocalDateTime.now().plusHours(24)); // Expiry time 1 day
            userRepo.save(user);

            // Send reset password email with the token
            emailService.sendResetPasswordEmail(email, code6);

            return ResponseEntity.ok("Password reset sent successfully.");
        } else {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }


    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam("code") String code,@Valid @RequestBody PasswordDto passwordDto) {


        // Find user by reset token
        User user = userRepo.findByResetCode(code);
        if (user != null && user.getResetCodeExpiry().isAfter(LocalDateTime.now())) {
            // Update user's password and clear reset token
            user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
//            user.setPassword(passwordDto.getPassword());
            user.setResetCode(null);
            user.setResetCodeExpiry(null);
            userRepo.save(user);

            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired code.");
        }
    }


}