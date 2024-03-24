package com.projects.socialapp.service;

import com.projects.socialapp.Repo.ChatRepo;
import com.projects.socialapp.mapper.ChatMapper;
import com.projects.socialapp.model.Chat;
import com.projects.socialapp.requestDto.ChatRequestDto;
import com.projects.socialapp.responseDto.ChatUserDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        var savedComment = chatRepo.save(chat);
        return chatMapper.toChatDto(savedComment);


    }

    @Transactional
    public Chat saveChat(Chat chat) {
        return chatRepo.save(chat);
    }

    @Override
    public List<Chat> findChatById(Integer chatId) throws Exception {
        Optional<Chat> optionalChat = chatRepo.findById(chatId);
        if(optionalChat.isEmpty())
        {
            throw new Exception("chat not found");
        }
        return (List<Chat>) optionalChat.get();
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
}
