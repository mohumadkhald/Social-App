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
public class ReelRequestDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String video;
    private Integer userId;
}
