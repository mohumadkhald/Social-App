package com.projects.socialapp.mapper;

import com.projects.socialapp.model.Post;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.PostRequestDto;
import com.projects.socialapp.responseDto.PostResponseDto;
import org.springframework.stereotype.Service;

@Service
public class PostMapper {
    public Post toPost(PostRequestDto postRequestDto)
    {
        var post = new Post();
        post.setCaption(postRequestDto.getCaption());
        post.setImage(postRequestDto.getImage());
        post.setVideo(postRequestDto.getVideo());
        var user = new User();
        user.setId(postRequestDto.getUserId());
        post.setUser(user);
        return post;
    }

    public PostResponseDto toPostDto(Post post)
    {
        return new PostResponseDto(post.getCaption(), post.getVideo(), post.getImage());
    }
}
