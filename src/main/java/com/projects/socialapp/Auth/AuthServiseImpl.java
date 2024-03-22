package com.projects.socialapp.Auth;

import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.config.JwtService;
import com.projects.socialapp.expection.EmailAlreadyExistsException;
import com.projects.socialapp.expection.UserNotFoundException;
import com.projects.socialapp.model.Role;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthServiseImpl implements AuthService {

    /*
    |--------------------------------------------------------------------------
    | Inject Classes
    |--------------------------------------------------------------------------
    |
    | Get Number of Followers And Following and Get Friends
    |
    */
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /*
    |--------------------------------------------------------------------------
    | Implement Register
    |--------------------------------------------------------------------------
    |
    | In her you can controller the data you want to enter to db
    |
    */
    @Override
    public AuthResponse register(RegisterRequestDto request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .gender(request.getGender())
                .build();
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        userRepo.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).message("Register Success Have A Nice Time").build();
    }

    /*
    |--------------------------------------------------------------------------
    | Implement Log in
    |--------------------------------------------------------------------------
    |
    | Take Data you need from auth and have all information to check any think you need
    |
    */
    @Override
    public AuthResponse login(LoginRequestDto request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Retrieve user details
            var user = userRepo.findByEmail(request.getEmail());

            // Generate JWT token
            var jwtToken = jwtService.generateToken(user);

            // Return the token along with the email
            return AuthResponse.builder().token(jwtToken).message("Login Success").build();
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("This Email Not Found Register First");
        }
    }
}
