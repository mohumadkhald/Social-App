package com.projects.socialapp.responseDto;

public record UserResponseDto(Integer id, String username,  String gender, String email, boolean verify, String role, boolean enabled, boolean non_locked) {
}