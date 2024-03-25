package com.projects.socialapp.requestDto;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private String content;

}
