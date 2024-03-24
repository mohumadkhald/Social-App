package com.projects.socialapp.expection;

public class ReelNotFoundException extends RuntimeException {
    public ReelNotFoundException(String message) {
        super(message);
    }
}
