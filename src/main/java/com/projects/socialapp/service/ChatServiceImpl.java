package com.projects.socialapp.service;

import com.projects.socialapp.Repo.ChatRepo;
import com.projects.socialapp.mapper.ChatMapper;
import com.projects.socialapp.model.Chat;
import com.projects.socialapp.requestDto.ChatRequestDto;
import com.projects.socialapp.responseDto.ChatUserDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService{
    private final ChatRepo chatRepo;
    private final ChatMapper chatMapper;

    public ChatServiceImpl(ChatRepo chatRepo, ChatMapper chatMapper) {
        this.chatRepo = chatRepo;
        this.chatMapper = chatMapper;
    }

    @Override
    public List<ChatUserDto> createChat(ChatRequestDto chatRequestDto) {
        var chat = chatMapper.toChat(chatRequestDto);
        var saved = chatRepo.save(chat);
        return chatMapper.toChatDto(saved, null);


    }

    @Override
    public Chat findById(Integer userId) {
        return null;
    }


    @Transactional
    public Chat saveChat(Chat chat) {
        return chatRepo.save(chat);
    }





    @Override
    public List<ChatUserDto> findUsersChat(Integer userId) {
        List<Chat> chats = chatRepo.findChatsByUserId(userId);

        // Create a list to store the converted DTOs
        List<ChatUserDto> chatUserDtos = new ArrayList<>();

        // Iterate over each Chat entity and convert it to DTO
        for (Chat chat : chats) {

            // Convert Chat entity to DTO using chatMapper, passing both the Chat and otherUser
            List<ChatUserDto> chatUserDtoList = chatMapper.toChatDto(chat, userId);
            chatUserDtos.addAll(chatUserDtoList); // Add all DTOs to the list
        }

        return chatUserDtos; // Return the list of DTOs
    }



    @Override
    public List<ChatUserDto> findChatById(Integer chatId, Integer userId) throws Exception {
        // Find the chat entity by chatId
        Chat chat = chatRepo.findChatById(chatId);

        // Check if chat is null
        if (chat == null) {
            throw new Exception("Chat not found for chatId: " + chatId);
        }

        // Convert the Chat entity to a list of ChatUserDto
        List<ChatUserDto> chatUserDtoList = chatMapper.toChatDto(chat, userId);

        // Check if the list is empty
        if (chatUserDtoList.isEmpty()) {
            throw new Exception("No ChatUserDto found for chatId: " + chatId);
        }

        // Return the list of ChatUserDto
        return chatUserDtoList;
    }


}
