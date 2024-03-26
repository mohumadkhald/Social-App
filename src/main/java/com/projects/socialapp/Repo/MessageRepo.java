package com.projects.socialapp.Repo;

import com.projects.socialapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Integer> {

    List<Message> findAllByChatId(Integer chatId);

    void deleteByChatId(Integer chatId);
}
