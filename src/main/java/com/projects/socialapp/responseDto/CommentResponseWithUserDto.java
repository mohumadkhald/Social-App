package com.projects.socialapp.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseWithUserDto {
    private Integer id;
    private String content;
    private String userName;

}