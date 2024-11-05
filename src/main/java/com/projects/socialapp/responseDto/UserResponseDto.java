package com.projects.socialapp.responseDto;

public record UserResponseDto(Integer id, String username,  String gender, String email, boolean verify) {
}