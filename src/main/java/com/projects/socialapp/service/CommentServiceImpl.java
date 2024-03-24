package com.projects.socialapp.service;

import com.projects.socialapp.Repo.CommentRepo;
import com.projects.socialapp.Repo.PostRepo;
import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.expection.CommentNotFoundException;
import com.projects.socialapp.expection.NotAuthorizeException;
import com.projects.socialapp.expection.PostNotFoundException;
import com.projects.socialapp.expection.UserNotFoundException;
import com.projects.socialapp.mapper.CommentMapper;
import com.projects.socialapp.model.Comment;
import com.projects.socialapp.model.Post;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.CommentRequestDto;
import com.projects.socialapp.responseDto.CommentResponseDto;
import com.projects.socialapp.responseDto.CommentResponseWithUserDto;
import com.projects.socialapp.traits.ApiTrait;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final ApiTrait apiTrait;
    private final UserRepo userRepo;
    private final CommentRepo commentRepo;
    private final CommentMapper commentMapper;
    private final PostRepo postRepo;

    @Override
    public CommentResponseDto createNewComment(CommentRequestDto commentRequestDto)
    {

        var comment = commentMapper.toComment(commentRequestDto);
        var savedComment = commentRepo.save(comment);
        return commentMapper.toCommentDto(savedComment);
    }

    @Override
    public CommentResponseDto editComment(CommentRequestDto commentRequestDto, Integer commentId, Integer postId, Integer userId) throws Exception {
        try {
            // Check if the comment ID is provided
            if (commentId == null || userId == null || postId == null) {
                throw new IllegalArgumentException("Comment ID or User ID or Post ID Messing.");
            }
            Comment comment = commentRepo.findById(commentId)
                    .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));
            if (!comment.getUser().getId().equals(userId)) {
                throw new NotAuthorizeException("You are not Authorized to Edit this comment");
            }

            // Retrieve the existing comment from the database based on its ID
            Optional<Comment> existingCommentOptional = commentRepo.findById(commentId);
            if (existingCommentOptional.isPresent()) {
                // Update the content of the existing comment
                Comment existingComment = existingCommentOptional.get();
                existingComment.setContent(commentRequestDto.getContent());



                // Save the updated comment
                Comment savedComment = commentRepo.save(existingComment);

                // Convert the saved comment to CommentResponseDto and return
                return commentMapper.toCommentDto(savedComment);
            } else {
                throw new CommentNotFoundException("Comment not found with ID: " + commentId);
            }
        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violation exceptions
            throw new Exception("Failed to edit comment: " + e.getMessage());
        }
    }

    @Override
    public String deleteComment(Integer commentId, Integer userId, Integer postId) {
        // Check if the comment ID is provided
        if (commentId == null || userId == null || postId == null) {
            throw new IllegalArgumentException("Comment ID or User ID or Post ID Messing.");
        }
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new CommentNotFoundException("post not found with id: " + postId));
        // Retrieve the comment by commentId
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // Check if the comment belongs to the specified userId
        if (!comment.getUser().getId().equals(userId)) {
            throw new NotAuthorizeException("You are not Authorized to Delete this comment");
        }
        // Delete the comment
        commentRepo.delete(comment);

        return "Comment deleted successfully";
    }


    @Override
    public List<CommentResponseWithUserDto> findAllCommentsByPostId(Integer postId) {
        List<Comment> comments = commentRepo.findAllByPostId(postId);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException("No comments found for user with id: " + postId);
        }

        List<CommentResponseWithUserDto> commentResponseList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseWithUserDto commentResponseDto = new CommentResponseWithUserDto();
            commentResponseDto.setId(comment.getId());
            commentResponseDto.setContent(comment.getContent());
            commentResponseDto.setUserName(comment.getUser().getFirstname() + " " + comment.getUser().getLastname());
            commentResponseList.add(commentResponseDto);
        }

        return commentResponseList;
    }






    @Override
    public ResponseEntity<?> likeUnlikeComment(Integer commentId, Integer userId, Integer postId) {
        // Check if the comment ID is provided
        if (commentId == null || userId == null || postId == null) {
            throw new IllegalArgumentException("Comment ID or User ID or Post ID Messing.");
        }
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        // Retrieve the comment by commentId
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // Retrieve the user by userId
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Check if the user has already liked the comment
        if (comment.getLikedByUsers().contains(user)) {
            // User has liked the comment, so unlike it
            comment.getLikedByUsers().remove(user);

            commentRepo.save(comment);
            // Return success response for unlike
            return ResponseEntity.ok().body("Comment unliked successfully");
        } else {
            // User has not liked the comment, so like it
            comment.getLikedByUsers().add(user);

            commentRepo.save(comment);
            // Return success response for like
            return ResponseEntity.ok().body("Comment liked successfully");
        }
    }

    @Override
    public CommentResponseWithUserDto findCommentById(Integer commentId, Integer postId) {
        // Check if the comment ID is provided
        if (commentId == null) {
            throw new IllegalArgumentException("Comment ID Missing.");
        }
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // Now fetch the associated user information
        User user = comment.getUser();

        // Create a DTO that includes both comment information and user's name
        CommentResponseWithUserDto commentResponseWithUserDto = new CommentResponseWithUserDto();
        commentResponseWithUserDto.setId(comment.getId());
        commentResponseWithUserDto.setContent(comment.getContent());
        commentResponseWithUserDto.setUserName(comment.getUser().getFirstname());

        return commentResponseWithUserDto;
    }

    @Override
    public ResponseEntity<?> getUsersWhoLikedComment(Integer commentId, Integer postId) {
        if (commentId == null || postId == null) {
            throw new IllegalArgumentException("Comment ID or Post ID Messing.");
        }
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        // Retrieve the comment by commentId
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + commentId));

        // Retrieve the information of users who liked the comment
        List<Map<String, Object>> likedUsersInfo = comment.getLikedByUsers().stream()
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


}
