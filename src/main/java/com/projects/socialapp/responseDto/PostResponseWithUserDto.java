package com.projects.socialapp.responseDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseWithUserDto {
    private Integer id;
    private String caption;
    private String video;
    private String image;
    private String userName;

}