package com.projects.socialapp.service;

import com.projects.socialapp.Repo.ReelRepo;
import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.expection.NotAuthorizeException;
import com.projects.socialapp.expection.PostNotFoundException;
import com.projects.socialapp.expection.ReelNotFoundException;
import com.projects.socialapp.expection.UserNotFoundException;
import com.projects.socialapp.mapper.ReelMapper;
import com.projects.socialapp.model.Reel;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.ReelRequestDto;
import com.projects.socialapp.responseDto.ReelResponseDto;
import com.projects.socialapp.responseDto.ReelResponseWithUserDto;
import com.projects.socialapp.traits.ApiTrait;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReelServiceImpl implements ReelService{
    private final ApiTrait apiTrait;
    private final UserRepo userRepo;
    private final ReelRepo reelRepo;
    private final ReelMapper reelMapper;

    @Override
    public ReelResponseDto createNewReel(ReelRequestDto reelRequestDto) throws Exception {

        var reel = reelMapper.toReel(reelRequestDto);
        var savedReel = reelRepo.save(reel);
        return reelMapper.toReelDto(savedReel);
    }

    @Override
    public ReelResponseDto editReel(ReelRequestDto reelRequestDto, Integer reelId, Integer userId) throws Exception {
        try {
            // Check if the comment ID is provided
            if (userId == null || reelId == null) {
                throw new IllegalArgumentException("User ID or Reel ID Messing.");
            }
            Reel reel = reelRepo.findById(reelId)
                    .orElseThrow(() -> new ReelNotFoundException("Reel not found with id: " + reelId));
            if (!reel.getUser().getId().equals(userId)) {
                throw new NotAuthorizeException("You are not Authorized to Edit this Reel");
            }

            // Retrieve the existing comment from the database based on its ID
            Optional<Reel> existingReelOptional = reelRepo.findById(reelId);
            if (existingReelOptional.isPresent()) {
                // Update the content of the existing comment
                Reel existingReel = existingReelOptional.get();
                existingReel.setTitle(reelRequestDto.getTitle());
                existingReel.setVideo(reelRequestDto.getVideo());



                // Save the updated comment
                Reel savedReel = reelRepo.save(existingReel);

                // Convert the saved comment to CommentResponseDto and return
                return reelMapper.toReelDto(savedReel);
            } else {
                throw new ReelNotFoundException("Reel not found with ID: " + reelId);
            }
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violation exceptions
            throw new Exception("Failed to edit comment: " + e.getMessage());
        }
    }

    @Override
    public String deleteReel(Integer reelId, Integer userId) throws AccessDeniedException {
        // Retrieve the reel by reelId
        Reel reel = reelRepo.findById(reelId)
                .orElseThrow(() -> new ReelNotFoundException("Reel not found with id: " + reelId));

        // Check if the reel belongs to the specified userId
        if (!reel.getUser().getId().equals(userId)) {
            throw new NotAuthorizeException("You are not Authorized to Delete this Reel");
        }
        // Delete the reel
        reelRepo.delete(reel);

        return "Reel deleted successfully";
    }


    @Override
    public List<ReelResponseWithUserDto> findReelsByUserId(Integer userId) {
        List<Reel> reels = reelRepo.findAllByUserId(userId);
        if (reels.isEmpty()) {
            throw new ReelNotFoundException("No reels found for user with id: " + userId);
        }

        List<ReelResponseWithUserDto> reelResponseList = new ArrayList<>();
        for (Reel reel : reels) {
            ReelResponseWithUserDto reelResponseDto = new ReelResponseWithUserDto();
            reelResponseDto.setId(reel.getId());
            reelResponseDto.setTitle(reel.getTitle());
            reelResponseDto.setVideo(reel.getVideo());
            reelResponseDto.setUserName(reel.getUser().getFirstname() + " " + reel.getUser().getLastname());
            reelResponseList.add(reelResponseDto);
        }

        return reelResponseList;
    }



    @Override
    public ReelResponseWithUserDto findReelByReelId(Integer reelId) {
        Reel reel = reelRepo.findById(reelId)
                .orElseThrow(() -> new ReelNotFoundException("Reel not found with id: " + reelId));

        // Now fetch the associated user information
        User user = reel.getUser();

        // Create a DTO that includes both reel information and user's name
        ReelResponseWithUserDto reelResponseWithUserDto = new ReelResponseWithUserDto();
        reelResponseWithUserDto.setId(reel.getId());
        reelResponseWithUserDto.setTitle(reel.getTitle());
        reelResponseWithUserDto.setVideo(reel.getVideo());
        reelResponseWithUserDto.setUserName(reel.getUser().getFirstname());

        return reelResponseWithUserDto;
    }

    @Override
    public List<ReelResponseWithUserDto> findAllReel() {
        List<Reel> reels = reelRepo.findAll();
        return getReelResponseWithUserDto(reels);
    }

    private List<ReelResponseWithUserDto> getReelResponseWithUserDto(List<Reel> reels) {
        List<ReelResponseWithUserDto> ReelResponseList = new ArrayList<>();
        for (Reel reel : reels) {
            ReelResponseWithUserDto reelResponseWithUserDto = new ReelResponseWithUserDto();
            reelResponseWithUserDto.setId(reel.getId());
            reelResponseWithUserDto.setTitle(reel.getTitle());
            reelResponseWithUserDto.setVideo(reel.getVideo());
            reelResponseWithUserDto.setUserName(reel.getUser().getFirstname());
            ReelResponseList.add(reelResponseWithUserDto);
        }

        return ReelResponseList;
    }

    @Override
    public Reel savedReel(Integer reelId, Integer userId) {
        return null;
    }



    @Override
    public ResponseEntity<?> getUsersWhoLikedReel(Integer reelId) {
        // Retrieve the reel by reelId
        Reel reel = reelRepo.findById(reelId)
                .orElseThrow(() -> new ReelNotFoundException("Reel not found with id: " + reelId));

        // Retrieve the information of users who liked the reel
        var likedUsersInfo = reel.getLikedByUsers().stream()
                .map(user -> {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("id", user.getId());
                    userInfo.put("fullName", user.getFirstname() + " " + user.getLastname());
                    return userInfo;
                })
                .collect(Collectors.toList());

        // Return the response
        Map<String, Object> response = new HashMap<>();
        response.put("likedUsers", likedUsersInfo);
        response.put("likeCount", likedUsersInfo.size()); // Like count
        return ResponseEntity.ok().body(response);
    }

    @Override
    public ResponseEntity<?> likeUnlikeReel(Integer reelId, Integer userId) {
        // Retrieve the reel by reelId
        Reel reel = reelRepo.findById(reelId)
                .orElseThrow(() -> new ReelNotFoundException("Reel not found with id: " + reelId));

        // Retrieve the user by userId
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Check if the user has already liked the reel
        if (reel.getLikedByUsers().contains(user)) {
            // User has liked the reel, so unlike it
            reel.getLikedByUsers().remove(user);

            reelRepo.save(reel);
            // Return success response for unlike
            return ResponseEntity.ok().body("Reel unliked successfully");
        } else {
            // User has not liked the reel, so like it
            reel.getLikedByUsers().add(user);

            reelRepo.save(reel);
            // Return success response for like
            return ResponseEntity.ok().body("Reel liked successfully");
        }
    }




    @Override
    public List<ReelResponseWithUserDto> findReelByTitle(String title)
    {
        List<Reel> reels = reelRepo.findAllByTitle(title);
        if (reels.isEmpty()) {
            throw new PostNotFoundException("No posts found for user with this name: " + title);
        }

        return getReelResponseWithUserDos(reels);
    }

    private List<ReelResponseWithUserDto> getReelResponseWithUserDos(List<Reel> reels) {
        List<ReelResponseWithUserDto> reelResponseList = new ArrayList<>();
        for (Reel reel : reels) {
            ReelResponseWithUserDto reelResponseWithUserDto = new ReelResponseWithUserDto();
            reelResponseWithUserDto.setId(reel.getId());
            reelResponseWithUserDto.setTitle(reel.getTitle());
            reelResponseWithUserDto.setVideo(reel.getVideo());
            reelResponseWithUserDto.setUserName(reel.getUser().getFirstname());
            reelResponseList.add(reelResponseWithUserDto);
        }

        return reelResponseList;
    }

}
