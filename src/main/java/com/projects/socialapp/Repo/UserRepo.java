package com.projects.socialapp.Repo;

import com.projects.socialapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    boolean existsByEmail(String email);

    User findUserById(Integer userId1);

}
