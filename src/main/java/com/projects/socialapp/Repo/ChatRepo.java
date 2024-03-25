package com.projects.socialapp.Repo;

import com.projects.socialapp.model.Chat;
import com.projects.socialapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c JOIN c.users u WHERE u.id = :userId")
    List<Chat> findChatsByUserId(Integer userId);    // Other repository methods...

    @Query("SELECT c FROM Chat c WHERE :user1 MEMBER OF c.users AND :user2 MEMBER OF c.users")
    public Chat findChatByUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT c FROM Chat c JOIN c.users u1 JOIN c.users u2 " +
            "WHERE u1.id = :userId1 AND u2.id = :userId2")
    Chat findChatByUserIds(@Param("userId1") Integer userId1, @Param("userId2") Integer userId2);


    Chat findChatById(Integer chatId);
}
