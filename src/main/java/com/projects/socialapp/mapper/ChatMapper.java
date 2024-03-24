package com.projects.socialapp.mapper;

import com.projects.socialapp.Repo.ChatRepo;
import com.projects.socialapp.model.Chat;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.ChatRequestDto;
import com.projects.socialapp.responseDto.ChatUserDto;
import com.projects.socialapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatMapper {
    private final UserService userService;
    private final ChatRepo chatRepo;
    public Chat toChat(ChatRequestDto chatRequestDto) {
        Integer userId1 = chatRequestDto.getUserId1();
        Integer userId2 = chatRequestDto.getUserId2();

        // Prevent creating a chat if userId1 and userId2 are the same
        if (userId1.equals(userId2)) {
            throw new IllegalArgumentException("User IDs must be different for creating a chat.");
        }

        // Check if a chat between the specified users already exists
        Chat existingChat = chatRepo.findChatByUserIds(userId1, userId2);
        if (existingChat != null) {
            return existingChat; // Return the existing chat if found
        }

        // If no existing chat found and user IDs are different, create a new chat
        Chat chat = new Chat();
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setId(userId1);

        User user2 = new User();
        user2.setId(userId2);

        users.add(user1);
        users.add(user2);

        chat.setUsers(users);
        chat.setChat_name(chatRequestDto.getTitle());

        return chat;
    }


    public List<ChatUserDto> toChatDto(Chat chat, Integer currentId) {

        List<ChatUserDto> chatUserDtos = new ArrayList<>();
        // Iterate over the list of users associated with the chat
        for (User user : chat.getUsers()) {
            if (user.getId().equals(currentId)) {
                continue; // Skip the current user
            }
            user = userService.findById(user.getId());
            // Assuming ChatRequestDto constructor accepts user IDs
            ChatUserDto chatUserDto = new ChatUserDto(chat.getId(),chat.getChat_name(), chat.getChat_img(), user.getId(), user.getFirstname(), user.getLastname());
            chatUserDtos.add(chatUserDto);
        }

        return chatUserDtos;
    }
}
