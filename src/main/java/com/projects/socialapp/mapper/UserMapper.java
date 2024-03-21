package com.projects.socialapp.mapper;

import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.UserRequestDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    // Make Set Data of Student As you Need Data in Record DTO
    public User toUser(UserRequestDto dto)
    {
        if (dto == null)
        {
            throw new NullPointerException("The Student Dto is Null");
        }
        var user = new User();
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setEmail(dto.getEmail());
        user.setGender(dto.getGender());
//        var schoolModel = new SchoolModel();
//        schoolModel.setId(dto.schoolId());
//        studentModel.setSchool(schoolModel);
        return user;
    }

    // Display Data as You need
    public UserResponseDto toUserResponseDto(User user)
    {
        return new UserResponseDto(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getGender(), user.getFollowers(), user.getFollowings());
    }
}
