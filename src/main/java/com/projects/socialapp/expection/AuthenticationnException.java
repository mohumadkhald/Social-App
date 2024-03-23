package com.projects.socialapp.expection;

public class AuthenticationnException extends RuntimeException {
    public AuthenticationnException(String message) {
        super(message);
    }
}