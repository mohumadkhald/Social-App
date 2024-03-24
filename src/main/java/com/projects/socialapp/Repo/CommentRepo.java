package com.projects.socialapp.Repo;

import com.projects.socialapp.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByPostId(Integer userId);
}
