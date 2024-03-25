package com.projects.socialapp.service;

import com.projects.socialapp.model.Chat;
import com.projects.socialapp.requestDto.ChatRequestDto;
import com.projects.socialapp.responseDto.ChatUserDto;

import java.util.List;

public interface ChatService {


    List<ChatUserDto> findUsersChat(Integer userId);


    List<ChatUserDto> createChat(ChatRequestDto chatRequestDto);

    Chat findById(Integer userId);

    List<ChatUserDto> findChatById(Integer chatId) throws Exception;
}
