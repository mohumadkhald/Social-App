package com.projects.socialapp.traits;

import com.projects.socialapp.responseDto.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ApiTrait {
    public ResponseEntity<?> successMessage(String message, HttpStatus code) {
        return ResponseEntity.status(code)
                .body(new ApiResponse(message, null, null));
    }

    public ResponseEntity<?> errorMessage(HashMap<String, String> errors, String message, HttpStatus code) {
        return ResponseEntity.status(code)
                .body(new ApiResponse(message, errors, null));
    }

    public ResponseEntity<?> data(List<UserResponseDto> data, String message, HttpStatus code) {
        return ResponseEntity.status(code)
                .body(new ApiResponse(message, null, data));
    }



    // Inner class representing the API response structure
    static class ApiResponse {
        private final String message;
        private final Object errors;
        private final Object data;

        public ApiResponse(String message, Object errors, Object data) {
            this.message = message;
            this.errors = errors;
            this.data = data;
        }

        // Getters for message, errors, and data
        // You can generate these using your IDE or write manually
        public String getMessage() {
            return message;
        }

        public Object getErrors() {
            return errors;
        }

        public Object getData() {
            return data;
        }
    }
}
