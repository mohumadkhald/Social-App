package com.projects.socialapp.responseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponseWithUserDto {
    private Integer id;
    private Integer chatId;
    private String content;
    private String image;
    private Integer senderId;
    private String sender;
    private String receiver;
    private String time;
}