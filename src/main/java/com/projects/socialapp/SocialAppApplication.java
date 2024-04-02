package com.projects.socialapp;

import com.projects.socialapp.Auth.PasswordResetController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class SocialAppApplication {
    @Autowired
    private PasswordResetController passwordResetController;

    public static void main(String[] args) {
        SpringApplication.run(SocialAppApplication.class, args);
    }

}
