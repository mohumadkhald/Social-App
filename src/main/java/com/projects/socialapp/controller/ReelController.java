package com.projects.socialapp.controller;

import com.projects.socialapp.requestDto.ReelRequestDto;
import com.projects.socialapp.responseDto.ReelResponseDto;
import com.projects.socialapp.responseDto.ReelResponseWithUserDto;
import com.projects.socialapp.service.ReelService;
import com.projects.socialapp.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reels")
@AllArgsConstructor
public class ReelController {
    private final ReelService reelService;
    private final UserService userService;

    @PostMapping
    public ReelResponseDto createReel(@Valid @RequestBody ReelRequestDto reelRequestDto, @RequestHeader("Authorization") String jwtToken) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        reelRequestDto.setUserId(userId);
        return reelService.createNewReel(reelRequestDto);
    }

    @GetMapping("/{reelId}")
    public ReelResponseWithUserDto getReelDetails(@PathVariable Integer reelId) {
        return reelService.findReelByReelId(reelId);
    }
    @PutMapping("/{reelId}")
    public ReelResponseDto editComment(@PathVariable Integer reelId, @Valid @RequestBody ReelRequestDto reelRequestDto, @RequestHeader("Authorization") String jwtToken) throws Exception{
        Integer userId = userService.findUserIdByJwt(jwtToken);
        reelRequestDto.setUserId(userId);
        return reelService.editReel(reelRequestDto, reelId, userId);
    }

    @GetMapping("/users/{userId}")
    public List<ReelResponseWithUserDto> getReelsByUserId(@PathVariable Integer userId) {
        return reelService.findReelsByUserId(userId);
    }

    @DeleteMapping("/{reelId}")
    public String deleteReel(@PathVariable Integer reelId, @RequestHeader("Authorization") String jwtToken) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return reelService.deleteReel(reelId, userId);
    }

    @GetMapping("/all")
    public List<ReelResponseWithUserDto> getAllReels() {
        return reelService.findAllReel();
    }


    @PostMapping("/{reelId}/like-unlike")
    public ResponseEntity<?> likeUnlikeReel(@PathVariable Integer reelId, @RequestHeader("Authorization") String jwtToken) {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return  reelService.likeUnlikeReel(reelId, userId);
    }


    @GetMapping("/{reelId}/likes")
    public ResponseEntity<?> getUsersWhoLikedReel(@PathVariable Integer reelId) {
        return reelService.getUsersWhoLikedReel(reelId);
    }

    @GetMapping("search/{title}")
    public List<ReelResponseWithUserDto> searchByTitle(@PathVariable String title) {
        return reelService.findReelByTitle(title);
    }
}
