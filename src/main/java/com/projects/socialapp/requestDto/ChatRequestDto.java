package com.projects.socialapp.requestDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDto {
    private Integer userId1;
    private Integer userId2;
    @NotEmpty
    private String title;
}
