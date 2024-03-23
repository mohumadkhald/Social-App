package com.projects.socialapp.service;

import com.projects.socialapp.model.Post;
import com.projects.socialapp.requestDto.PostRequestDto;
import com.projects.socialapp.responseDto.PostResponseDto;
import com.projects.socialapp.responseDto.PostResponseWithUserDto;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface PostService {
    PostResponseDto createNewPost(PostRequestDto post) throws Exception;
    String deletePost(Integer postId, Integer userId) throws AccessDeniedException;

    List<PostResponseWithUserDto> findPostsByUserId(Integer userId);

    PostResponseWithUserDto findPostByPostId(Integer PostId);
    List<Post> findAllPost();
    Post savedPost(Integer postId, Integer userId);


    ResponseEntity<?> getUsersWhoLikedPost(Integer postId);

    ResponseEntity<?> likeUnlikePost(Integer postId, Integer userId);

    // Ensure transactional operation
}
