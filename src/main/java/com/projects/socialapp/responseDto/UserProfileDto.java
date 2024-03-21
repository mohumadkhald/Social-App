package com.projects.socialapp.responseDto;

public record UserProfileDto(UserResponseDto user, int followers, int following, int friends) {
    public static UserProfileDto fromUserResponseDto(UserResponseDto userResponseDto, int followers, int following, int friends) {
        return new UserProfileDto(userResponseDto, followers, following, friends);
    }
}