package com.projects.socialapp.controller;

import com.projects.socialapp.requestDto.CommentRequestDto;
import com.projects.socialapp.responseDto.CommentResponseDto;
import com.projects.socialapp.responseDto.CommentResponseWithUserDto;
import com.projects.socialapp.service.CommentService;
import com.projects.socialapp.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @PostMapping
    public CommentResponseDto createComment(@PathVariable Integer postId, @Valid @RequestBody CommentRequestDto commentRequestDto, @RequestHeader("Authorization") String jwtToken) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        commentRequestDto.setUserId(userId);
        commentRequestDto.setPostId(postId);

        return commentService.createNewComment(commentRequestDto);
    }
    
    @PutMapping("/{commentId}")
    public CommentResponseDto editComment(@PathVariable Integer postId, @Valid @RequestBody CommentRequestDto commentRequestDto, @RequestHeader("Authorization") String jwtToken, @PathVariable Integer commentId) throws Exception{
        Integer userId = userService.findUserIdByJwt(jwtToken);
        commentRequestDto.setUserId(userId);
        commentRequestDto.setPostId(postId);
        commentRequestDto.setId(commentId);
        return commentService.editComment(commentRequestDto, commentId, postId, userId);
    }


    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Integer commentId, @RequestHeader("Authorization") String jwtToken, @PathVariable Integer postId) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return commentService.deleteComment(commentId, userId, postId);
    }

    @GetMapping("/all")
    public List<CommentResponseWithUserDto> getAllComments(@PathVariable Integer postId) {
        return commentService.findAllCommentsByPostId(postId);
    }


    @PostMapping("/{commentId}/like-unlike")
    public ResponseEntity<?> likeUnlikeComment(@PathVariable Integer commentId, @RequestHeader("Authorization") String jwtToken, @PathVariable Integer postId) {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return  commentService.likeUnlikeComment(commentId, userId, postId);
    }


    @GetMapping("/{commentId}/likes")
    public ResponseEntity<?> getUsersWhoLikedComment(@PathVariable Integer commentId, @PathVariable Integer postId) {
        return commentService.getUsersWhoLikedComment(commentId, postId);
    }


}
