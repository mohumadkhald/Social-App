package com.projects.socialapp.service;

import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.UserRequestDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {


    // Method Create User
    UserResponseDto registerUser(UserRequestDto dto);

    // Method Get User By ID
    ResponseEntity<?> getUserByIdResponse(Long id);

    // Method Get User By Email
    ResponseEntity<?> getUserByEmailResponse(String email);

    // Method Followers

    /*
    |--------------------------------------------------------------------------
    | Implement Followers
    |--------------------------------------------------------------------------
    |
    | Here is How you can get all Users Using List And Hash Map
    |
    */

    /*
    |--------------------------------------------------------------------------
    | Implement Followers
    |--------------------------------------------------------------------------
    |
    | Here is How you can get all Users Using List And Hash Map
    |
    */

    /*
    |--------------------------------------------------------------------------
    | Implement Followers
    |--------------------------------------------------------------------------
    |
    | Here is How you can get all Users Using List And Hash Map
    |
    */

    /*
    |--------------------------------------------------------------------------
    | Implement Followers
    |--------------------------------------------------------------------------
    |
    | Here is How you can get all Users Using List And Hash Map
    |
    */
    ResponseEntity<?> followUser(Long userId1, Long userId2);

    // Method search user
    public List<User> searchUser(String query);



    ResponseEntity<?> getAllUsers();


    ResponseEntity<?> updateUser(Long id, UserRequestDto dto);
}
