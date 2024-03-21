package com.projects.socialapp.expection;

import com.projects.socialapp.traits.ApiTrait;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;

@ControllerAdvice
@RestController

public class UserException {

    private final ApiTrait apiTrait;

    public UserException(ApiTrait apiTrait) {
        this.apiTrait = apiTrait;
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex, WebRequest request) {
        HashMap<String, String> error = new HashMap<>();
        error.put("email", "Email Already Exists"); // Assuming "email" is the field causing the error
        return (ResponseEntity<Object>) apiTrait.errorMessage(error, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        HashMap<String, String> error = new HashMap<>();
        error.put("user", "User Not Found"); // Assuming "user" is the field causing the error
        return (ResponseEntity<Object>) apiTrait.errorMessage(error, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getAllErrors().forEach(
                error -> {
                    var fieldName = ((FieldError) error).getField();
                    var errMsg = error.getDefaultMessage();
                    errors.put(fieldName, errMsg);
                }
        );
        return apiTrait.errorMessage(errors, "Validation Failed", HttpStatus.BAD_REQUEST);
    }

}