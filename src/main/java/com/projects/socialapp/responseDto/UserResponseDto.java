package com.projects.socialapp.responseDto;

import com.projects.socialapp.model.User;

import java.util.List;

public record UserResponseDto(Long id, String firstname, String lastname, String email, String gender,  List<Integer> followers, List<Integer>followings) {
}