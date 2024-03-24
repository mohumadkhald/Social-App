package com.projects.socialapp.service;

import com.projects.socialapp.requestDto.CommentRequestDto;
import com.projects.socialapp.responseDto.CommentResponseDto;
import com.projects.socialapp.responseDto.CommentResponseWithUserDto;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CommentService {
    CommentResponseDto createNewComment(CommentRequestDto commentRequestDto) throws Exception;

    CommentResponseDto editComment(CommentRequestDto commentRequestDto, Integer commentId, Integer postId, Integer userId) throws Exception;

    String deleteComment(Integer commentId, Integer userId, Integer postId) throws AccessDeniedException;

    List<CommentResponseWithUserDto> findAllCommentsByPostId(Integer userId);

    ResponseEntity<?> getUsersWhoLikedComment(Integer commentId, Integer postId);

    ResponseEntity<?> likeUnlikeComment(Integer commentId, Integer userId, Integer postId);

    CommentResponseWithUserDto findCommentById(Integer commentId, Integer postId);


    // Ensure transactional operation
}
