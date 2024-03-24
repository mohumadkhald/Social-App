package com.projects.socialapp.service;

import com.projects.socialapp.Repo.PostRepo;
import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.expection.CommentNotFoundException;
import com.projects.socialapp.expection.NotAuthorizeException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public PostResponseDto editPost(PostRequestDto postRequestDto, Integer postId, Integer userId) throws Exception {
        try {
            // Check if the comment ID is provided
            if (userId == null || postId == null) {
                throw new IllegalArgumentException("User ID or Post ID Messing.");
            }
            Post post = postRepo.findById(postId)
                    .orElseThrow(() -> new CommentNotFoundException("Post not found with id: " + postId));
            if (!post.getUser().getId().equals(userId)) {
                throw new NotAuthorizeException("You are not Authorized to Edit this Post");
            }

            // Retrieve the existing comment from the database based on its ID
            Optional<Post> existingPostOptional = postRepo.findById(postId);
            if (existingPostOptional.isPresent()) {
                // Update the content of the existing comment
                Post existingPost = existingPostOptional.get();
                existingPost.setCaption(postRequestDto.getCaption());
                existingPost.setCaption(postRequestDto.getImage());
                existingPost.setVideo(postRequestDto.getVideo());



                // Save the updated comment
                Post savedPost = postRepo.save(existingPost);

                // Convert the saved comment to CommentResponseDto and return
                return postMapper.toPostDto(savedPost);
            } else {
                throw new PostNotFoundException("Post not found with ID: " + postId);
            }
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violation exceptions
            throw new Exception("Failed to edit comment: " + e.getMessage());
        }
    }

    @Override
    public String deletePost(Integer postId, Integer userId)
    {
        // Retrieve the post by postId
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

        // Check if the post belongs to the specified userId
        if (!post.getUser().getId().equals(userId)) {
            throw new NotAuthorizeException("You are not Authorized to Delete this Post");
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

        return getPostResponseWithUserDto(posts);
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
    public List<PostResponseWithUserDto> findAllPost() {
        List<Post> posts = postRepo.findAll();

        return getPostResponseWithUserDto(posts);
    }

    private List<PostResponseWithUserDto> getPostResponseWithUserDto(List<Post> posts) {
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

    @Override
    public List<PostResponseWithUserDto> findPostByCaption(String caption)
    {
        List<Post> posts = postRepo.findAllByCaption(caption);
        if (posts.isEmpty()) {
            throw new PostNotFoundException("No posts found for user with this name: " + caption);
        }

        return getPostResponseWithUserDto(posts);
    }



}
