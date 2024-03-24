package com.projects.socialapp.expection;

import com.projects.socialapp.traits.ApiTrait;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@ControllerAdvice
@RestController

public class ReelException {

    public ReelException(ApiTrait apiTrait) {
    }



    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(PostNotFoundException ex, WebRequest request) {
        HashMap<String, String> error = new HashMap<>();
        error.put("Post", "Post Not Found"); // Assuming "user" is the field causing the error
        return  ApiTrait.errorMessage(error, ex.getMessage(), HttpStatus.NOT_FOUND);
    }



}