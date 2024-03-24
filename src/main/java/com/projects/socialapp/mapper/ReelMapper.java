package com.projects.socialapp.mapper;

import com.projects.socialapp.model.Reel;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.ReelRequestDto;
import com.projects.socialapp.responseDto.ReelResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ReelMapper {
    public Reel toReel(ReelRequestDto reelRequestDto)
    {
        var reel = new Reel();
        reel.setTitle(reelRequestDto.getTitle());
        reel.setVideo(reelRequestDto.getVideo());
        var user = new User();
        user.setId(reelRequestDto.getUserId());
        reel.setUser(user);
        return reel;
    }

    public ReelResponseDto toReelDto(Reel reel)
    {
        return new ReelResponseDto(reel.getTitle(), reel.getVideo());
    }
}
