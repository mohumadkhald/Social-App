package com.projects.socialapp.requestDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto{
        private Long id;
        @NotEmpty // custom message in here (message = "your message")
        String firstname;
        @NotEmpty
        String lastname;
        @NotEmpty
        String email;
        @NotNull
        String gender;
        @NotNull
        String password;
        private List<Integer> followers;
        private List<Integer>followings;

}