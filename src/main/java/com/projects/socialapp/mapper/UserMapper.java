package com.projects.socialapp.mapper;

import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.UserRequestDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        var schoolModel = new SchoolModel();
//        schoolModel.setId(dto.schoolId());
//        studentModel.setSchool(schoolModel);
        return user;
    }

    // Display Data as You need
    public UserResponseDto toUserResponseDto(User user) {
//        Set<Integer> followerIds = user.getFollowers().stream()
//                .map(User::getId)
//                .collect(Collectors.toSet());
//        Set<Integer> followingIds = user.getFollowings().stream()
//                .map(User::getId)
//                .collect(Collectors.toSet());

        return new UserResponseDto(user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getGender()
//                , followerIds,
//                followingIds
        );
    }
}
