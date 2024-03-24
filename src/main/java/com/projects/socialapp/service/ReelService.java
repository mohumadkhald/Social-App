package com.projects.socialapp.service;

import com.projects.socialapp.model.Reel;
import com.projects.socialapp.requestDto.ReelRequestDto;
import com.projects.socialapp.responseDto.ReelResponseDto;
import com.projects.socialapp.responseDto.ReelResponseWithUserDto;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ReelService {
    ReelResponseDto createNewReel(ReelRequestDto reel) throws Exception;

    ReelResponseDto editReel(ReelRequestDto reelRequestDto, Integer reelId, Integer userId) throws Exception;

    String deleteReel(Integer reelId, Integer userId) throws AccessDeniedException;

    List<ReelResponseWithUserDto> findReelsByUserId(Integer userId);

    ReelResponseWithUserDto findReelByReelId(Integer ReelId);
    List<ReelResponseWithUserDto> findAllReel();
    Reel savedReel(Integer reelId, Integer userId);


    ResponseEntity<?> getUsersWhoLikedReel(Integer reelId);

    ResponseEntity<?> likeUnlikeReel(Integer reelId, Integer userId);

    List<ReelResponseWithUserDto> findReelByTitle(String title);

    // Ensure transactional operation
}
