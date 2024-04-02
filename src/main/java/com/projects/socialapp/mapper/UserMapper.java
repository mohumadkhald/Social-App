package com.projects.socialapp.mapper;

import com.projects.socialapp.model.Role;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.RegisterRequestDto;
import com.projects.socialapp.responseDto.UserResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    // Make Set Data of Student As you Need Data in Record DTO
    public User toUser(RegisterRequestDto dto)
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
        user.setRole(Role.USER);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setPhone(dto.getPhone());
        return user;
    }

    // Display Data as You need
    public UserResponseDto toUserResponseDto(User user) {

        return new UserResponseDto(user.getId(),
                user.getFirstname() + " " +
                user.getLastname(),
                user.getGender(),
                user.getEmail()
        );
    }
}
