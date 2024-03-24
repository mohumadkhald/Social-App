package com.projects.socialapp.controller;

import com.projects.socialapp.requestDto.PostRequestDto;
import com.projects.socialapp.responseDto.PostResponseDto;
import com.projects.socialapp.responseDto.PostResponseWithUserDto;
import com.projects.socialapp.service.PostService;
import com.projects.socialapp.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @PostMapping
    public PostResponseDto createPost(@Valid @RequestBody PostRequestDto postRequestDto, @RequestHeader("Authorization") String jwtToken) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        postRequestDto.setUserId(userId);
        return postService.createNewPost(postRequestDto);
    }

    @GetMapping("/{postId}")
    public PostResponseWithUserDto getPostDetails(@PathVariable Integer postId) {
        return postService.findPostByPostId(postId);
    }
    @PutMapping("/{postId}")
    public PostResponseDto editComment(@PathVariable Integer postId, @Valid @RequestBody PostRequestDto postRequestDto, @RequestHeader("Authorization") String jwtToken) throws Exception{
        Integer userId = userService.findUserIdByJwt(jwtToken);
        postRequestDto.setUserId(userId);
        return postService.editPost(postRequestDto, postId, userId);
    }

    @GetMapping("/users/{userId}")
    public List<PostResponseWithUserDto> getPostsByUserId(@PathVariable Integer userId) {
        return postService.findPostsByUserId(userId);
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable Integer postId, @RequestHeader("Authorization") String jwtToken) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return postService.deletePost(postId, userId);
    }

    @GetMapping("/all")
    public List<PostResponseWithUserDto> getAllPosts() {
        return postService.findAllPost();
    }


    @PostMapping("/{postId}/like-unlike")
    public ResponseEntity<?> likeUnlikePost(@PathVariable Integer postId, @RequestHeader("Authorization") String jwtToken) {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return  postService.likeUnlikePost(postId, userId);
    }


    @GetMapping("/{postId}/likes")
    public ResponseEntity<?> getUsersWhoLikedPost(@PathVariable Integer postId) {
        return postService.getUsersWhoLikedPost(postId);
    }



}
