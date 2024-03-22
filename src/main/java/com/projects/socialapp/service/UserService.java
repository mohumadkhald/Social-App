package com.projects.socialapp.service;

import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import com.projects.socialapp.responseDto.UserProfileDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    Integer findUserIdByJwt(String jwt);

    // Method Create User
    UserResponseDto registerUser(RegisterRequestDto dto);

    // Method Get User By ID
    ResponseEntity<?> getUserByIdResponse(Integer id);

    // Method Get User By Email
    ResponseEntity<?> getUserByEmailResponse(String email);


    ResponseEntity<?> followUser(Integer userId1, Integer userId2);

    // Method search user
    List<User> searchUser(String query);



    ResponseEntity<?> getAllUsers();


    ResponseEntity<?> updateUser(Integer id, RegisterRequestDto dto);

    UserProfileDto getUserProfile(Integer userId);

    ResponseEntity<?> unfollowUser(Integer userId1, Integer userId2);
}
