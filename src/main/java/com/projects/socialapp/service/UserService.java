package com.projects.socialapp.service;

import com.projects.socialapp.requestDto.RegisterRequestDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import org.springframework.http.ResponseEntity;

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
    ResponseEntity<?> searchUser(String query);



    ResponseEntity<?> getAllUsers();


    ResponseEntity<?> updateUser(Integer id, RegisterRequestDto dto);

    ResponseEntity<?> getUserProfile(Integer userId);

    ResponseEntity<?> unfollowUser(Integer userId1, Integer userId2);


    ResponseEntity<?> getUserFollowers(Integer userId);

    ResponseEntity<?> getUserFollowing(Integer userId);

    ResponseEntity<?> getUserFriends(Integer userId);
}
