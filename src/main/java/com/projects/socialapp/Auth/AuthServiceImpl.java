package com.projects.socialapp.Auth;

import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.config.JwtProvider;
import com.projects.socialapp.expection.EmailAlreadyExistsException;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.UserRequestDto;
import com.projects.socialapp.responseDto.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomServiceImpl customService;


    @Override
    public User findUserProfileByJwt(String jwt)
    {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepo.findByEmail(email);

        if (user == null)
        {
            return null;
        }
        // Implementation to find a user's profile by JWT token
        // You need to provide the logic to retrieve the user's profile based on the JWT token
        return user; // Placeholder return value
    }

    @Override
    public Integer findUserIdByJwt(String jwt)
    {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepo.findByEmail(email);

        if (user == null) {
            return null;
        }

        return user.getId();
    }

    @Override
    public ResponseEntity<?> createUserHandeler(UserRequestDto user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already used with another account: {}");
        }

        User createdUser = createUserFromRequest(user);
        User savedUser = userRepo.save(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(token, "Signup Success");
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @Override
    public ResponseEntity<?> login(LoginRequestDto loginRequest) {
        // Retrieve user details from the database based on the provided email
        UserDetails userDetails = customService.loadUserByUsername(loginRequest.getEmail());

        // Validate the provided password against the user's password
        if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email/password ");
        }

        // Authenticate the user
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String token = jwtProvider.generateToken(authentication);

        // Return the JWT token along with a success message
        return ResponseEntity.ok(new AuthResponse(token, "Login Success"));
    }

    private User createUserFromRequest(UserRequestDto user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setGender(user.getGender());
        return newUser;
    }
}
