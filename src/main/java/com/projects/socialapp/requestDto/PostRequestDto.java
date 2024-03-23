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
public class PostRequestDto {
    @NotEmpty
    private String caption;
    @NotEmpty
    private String image;
    private String video;
    Integer userId;
}
