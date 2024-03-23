package com.projects.socialapp.Repo;

import com.projects.socialapp.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserId(Integer userId);
}
