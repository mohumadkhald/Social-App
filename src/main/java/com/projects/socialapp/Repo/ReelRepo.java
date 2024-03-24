package com.projects.socialapp.Repo;

import com.projects.socialapp.model.Reel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReelRepo extends JpaRepository<Reel, Integer> {
    List<Reel> findAllByUserId(Integer userId);

    List<Reel> findAllByTitle(String title);
}
