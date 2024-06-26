package com.projects.socialapp.Auth;

import com.projects.socialapp.Config.JwtService;
import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.expection.AuthenticationnException;
import com.projects.socialapp.expection.EmailAlreadyExistsException;
import com.projects.socialapp.model.Role;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.LoginRequestDto;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import com.projects.socialapp.token.Token;
import com.projects.socialapp.token.TokenRepo;
import com.projects.socialapp.token.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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
    private final TokenRepo tokenRepo;
    private EmailService emailService;
    /*
    |--------------------------------------------------------------------------
    | Implement Register
    |--------------------------------------------------------------------------
    |
    | In her you can Controller the data you want to enter to db
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
                .phone(request.getPhone())
                .accountNonExpired(true) // Set accountNonExpired property
                .accountNonLocked(true) // Set accountNonLocked property
                .credentialsNonExpired(true) // Set credentialsNonExpired property
                .verificationToken(UUID.randomUUID().toString()) // Generate verification token
                .verificationTokenExpiry(LocalDateTime.now().plusHours(24)) // Set expiry time (1 day)
                .build();
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        var savedUser = userRepo.save(user);
        int expirationDay = getExpirationDay(false);
        var jwtToken = jwtService.generateToken(user, expirationDay);
        savedUserToken(savedUser, jwtToken, false);
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());
        return AuthResponse.builder().token(jwtToken).message("Register Success Have A Nice Time").build();
    }


    @Override
    public ResponseEntity<String> resendVerificationEmail(String email) throws Exception {
        User user = userRepo.findByEmail(email);

        if (user.isEmailVerified()) {
            throw new Exception("User is already verified");
        }
         user.setVerificationToken(UUID.randomUUID().toString()); // Generate verification token
         user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24)); // Set expiry time (1 day)
        userRepo.save(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
        return ResponseEntity.ok("email send success");
    }




    /*
    |--------------------------------------------------------------------------
    | Implement Log in
    |--------------------------------------------------------------------------
    |
    | Take Data you need from auth and have all information to check any think you need
    |
    */
    public AuthResponse login(LoginRequestDto request) throws Exception {
        try {


            // Authenticate user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Retrieve user details
            var user = userRepo.findByEmail(request.getEmail());

            int expirationDay = getExpirationDay(request.isRemember());

            // Generate JWT token
            var jwtToken = jwtService.generateToken(user, expirationDay);
            savedUserToken(user, jwtToken, request.isRemember());

            if (!user.isEmailVerified())
            {
                return AuthResponse.builder().token(jwtToken).message("Login Success Email " + user.getEmail() + " Not Verify").build();
            }
            // Return the token along with the user details
            return AuthResponse.builder().token(jwtToken).message("Login Success").build();
        } catch (AuthenticationException e) {

            String errorMessage = getString(e);
            throw new AuthenticationnException(errorMessage) {

            };
        }
    }

    // error message when error in login
    private static String getString(AuthenticationException e) {
        String errorMessage = e.getMessage();
        if (Objects.equals(errorMessage, "UserDetailsService returned null, which is an interface contract violation")) {
            errorMessage = "You Are Not Register Yet";
        } else if (Objects.equals(errorMessage, "User credentials have expired")) {
            errorMessage = "User credentials have expired Please Reset Your Password";
        } else if (Objects.equals(errorMessage, "User account has expired")) {
            errorMessage = "User account has expired Please Contact Support Service";
        } else if (Objects.equals(errorMessage, "User account is locked")) {
            errorMessage = "User account is locked Please Contact Support Service";
        } else if (Objects.equals(errorMessage, "Bad credentials")) {
            errorMessage = "Invalid Credentials";
        }
        return errorMessage;
    }


    // Saved any token when user register or login
    private void savedUserToken(User user, String jwtToken, boolean status) {
        var myToken = Token.builder()
                .user(user)
                .token(jwtToken)
                .rememberMe(status)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepo.save(myToken);
    }

    // set expiry date for token
    private static int getExpirationDay(boolean request) {
        int expirationDay;
        if (request)
        {
            expirationDay = 1000 * 60 * 60 * 24 * 7; // check Remember Me token Valid 7 Days or when logout
        } else {
            expirationDay = 1000 * 60 * 60 * 24; // If Not check RememberMe token valid 24 Hour or when logout
        }
        return expirationDay;
    }


    // if you need make only for user one token call this method on login
    private void revokeAllUserToken(User user)
    {
        var validTokensForUser = tokenRepo.findAllValidTokenByUser(user.getId());
        if (validTokensForUser.isEmpty())
            return;
        validTokensForUser.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepo.saveAll(validTokensForUser);
    }



}
