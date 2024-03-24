package com.projects.socialapp.mapper;

import com.projects.socialapp.model.Comment;
import com.projects.socialapp.model.Post;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.CommentRequestDto;
import com.projects.socialapp.responseDto.CommentResponseDto;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {
    public Comment toComment(CommentRequestDto commentRequestDto)
    {
        var comment = new Comment();
        comment.setContent(commentRequestDto.getContent());
        var user = new User();
        user.setId(commentRequestDto.getUserId());
        var post = new Post();
        post.setId(commentRequestDto.getPostId());
        comment.setUser(user);
        comment.setPost(post);
        return comment;
    }

    public CommentResponseDto toCommentDto(Comment comment)
    {
        return new CommentResponseDto(comment.getContent());
    }
}
