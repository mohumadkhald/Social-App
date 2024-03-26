package com.projects.socialapp.requestDto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {
    private Integer sender;
    private Integer receiver;
    private Integer chatId;
    private String img;
    private String content;

}
