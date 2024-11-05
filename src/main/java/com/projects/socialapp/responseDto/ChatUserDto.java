package com.projects.socialapp.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatUserDto {
    private Integer chatID;
    private String chatName;
    private String img_path;
    private Integer id;
    private String username;
}