package com.projects.socialapp.service;

import com.projects.socialapp.requestDto.MessageRequestDto;
import com.projects.socialapp.responseDto.MessageResponseWithUserDto;

import java.util.List;

public interface MessageService {
    MessageResponseWithUserDto createNewMessage(MessageRequestDto post) throws Exception;

    List<MessageResponseWithUserDto> findAllMessagesByChatId(Integer chatId, Integer userId) throws Exception;

    String deleteMessage(Integer messageId, Integer userId);

    String deleteAllMessageByChatId(Integer userId, Integer chatId) throws Exception;

//    MessageResponseWithUserDto findMessageByMessageId(Integer messageId);
}
