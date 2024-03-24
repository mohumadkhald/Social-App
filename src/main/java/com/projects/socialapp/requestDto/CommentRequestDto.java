package com.projects.socialapp.requestDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {
    @NotEmpty
    private String content;

//    @NotEmpty
//    private String image;
//    private String video;
    Integer id;
    Integer userId;
    Integer postId;
}
