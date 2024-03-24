package com.projects.socialapp.service;

import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    Integer findUserIdByJwt(String jwt);

    User findById(Integer id);

    // Method Create User
    UserResponseDto registerUser(RegisterRequestDto dto);

    // Method Get User By ID
    ResponseEntity<?> getUserByIdResponse(Integer id);

    // Method Get User By Email
    ResponseEntity<?> getUserByEmailResponse(String email);

    // Method search user
    ResponseEntity<?> searchUser(String query);

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> updateUser(Integer id, RegisterRequestDto dto);

    ResponseEntity<?> getUserProfile(Integer userId);

    ResponseEntity<?> toggleFollowUser(Integer userId1, Integer userId2);

    ResponseEntity<?> getUserFollowers(Integer userId);

    ResponseEntity<?> getUserFollowing(Integer userId);

    ResponseEntity<?> getUserFriends(Integer userId);

}
