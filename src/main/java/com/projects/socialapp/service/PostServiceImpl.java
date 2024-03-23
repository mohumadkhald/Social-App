package com.projects.socialapp.service;

import com.projects.socialapp.Repo.PostRepo;
import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.expection.PostNotFoundException;
import com.projects.socialapp.expection.UserNotFoundException;
import com.projects.socialapp.mapper.PostMapper;
import com.projects.socialapp.model.Post;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.PostRequestDto;
import com.projects.socialapp.responseDto.PostResponseDto;
import com.projects.socialapp.responseDto.PostResponseWithUserDto;
import com.projects.socialapp.traits.ApiTrait;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService{
    private final ApiTrait apiTrait;
    private final UserRepo userRepo;
    private final PostRepo postRepo;
    private final PostMapper postMapper;

    @Override
    public PostResponseDto createNewPost(PostRequestDto postRequestDto) throws Exception {

        var post = postMapper.toPost(postRequestDto);
        var savedPost = postRepo.save(post);
        return postMapper.toPostDto(savedPost);
    }

    @Override
    public String deletePost(Integer postId, Integer userId) throws AccessDeniedException {
        // Retrieve the post by postId
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // Check if the post belongs to the specified userId
        if (!post.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to delete this post");
        }
        // Delete the post
        postRepo.delete(post);

        return "Post deleted successfully";
    }


    @Override
    public List<PostResponseWithUserDto> findPostsByUserId(Integer userId) {
        List<Post> posts = postRepo.findAllByUserId(userId);
        if (posts.isEmpty()) {
            throw new PostNotFoundException("No posts found for user with id: " + userId);
        }

        List<PostResponseWithUserDto> postResponseList = new ArrayList<>();
        for (Post post : posts) {
            PostResponseWithUserDto postResponseDto = new PostResponseWithUserDto();
            postResponseDto.setId(post.getId());
            postResponseDto.setCaption(post.getCaption());
            postResponseDto.setVideo(post.getVideo());
            postResponseDto.setImage(post.getImage());
            postResponseDto.setUserName(post.getUser().getFirstname());
            postResponseList.add(postResponseDto);
        }

        return postResponseList;
    }



    @Override
    public PostResponseWithUserDto findPostByPostId(Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // Now fetch the associated user information
        User user = post.getUser();

        // Create a DTO that includes both post information and user's name
        PostResponseWithUserDto postResponseWithUserDto = new PostResponseWithUserDto();
        postResponseWithUserDto.setId(post.getId());
        postResponseWithUserDto.setCaption(post.getCaption());
        postResponseWithUserDto.setVideo(post.getVideo());
        postResponseWithUserDto.setImage(post.getImage());
        postResponseWithUserDto.setUserName(post.getUser().getFirstname());

        return postResponseWithUserDto;
    }

    @Override
    public List<Post> findAllPost() {
        return postRepo.findAll();
    }

    @Override
    public Post savedPost(Integer postId, Integer userId) {
        return null;
    }



    @Override
    public ResponseEntity<?> getUsersWhoLikedPost(Integer postId) {
        // Retrieve the post by postId
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // Retrieve the information of users who liked the post
        List<Map<String, Object>> likedUsersInfo = post.getLikedByUsers().stream()
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
    public ResponseEntity<?> likeUnlikePost(Integer postId, Integer userId) {
        // Retrieve the post by postId
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // Retrieve the user by userId
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Check if the user has already liked the post
        if (post.getLikedByUsers().contains(user)) {
            // User has liked the post, so unlike it
            post.getLikedByUsers().remove(user);

            postRepo.save(post);
            // Return success response for unlike
            return ResponseEntity.ok().body("Post unliked successfully");
        } else {
            // User has not liked the post, so like it
            post.getLikedByUsers().add(user);

            postRepo.save(post);
            // Return success response for like
            return ResponseEntity.ok().body("Post liked successfully");
        }
    }




}
