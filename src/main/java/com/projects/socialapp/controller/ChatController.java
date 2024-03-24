package com.projects.socialapp.controller;

import com.projects.socialapp.model.Chat;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.ChatRequestDto;
import com.projects.socialapp.responseDto.ChatUserDto;
import com.projects.socialapp.service.ChatService;
import com.projects.socialapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/chat")
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/{userId2}")
    public List<ChatUserDto> createChat(@PathVariable Integer userId2, @RequestHeader("Authorization") String jwtToken, @Valid @RequestBody ChatRequestDto chatRequestDto)
    {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        User user1 = userService.findById(userId);
        User user2 = userService.findById(userId2);
        chatRequestDto.setUserId1(userId);
        chatRequestDto.setUserId2(userId2);



        return chatService.createChat(chatRequestDto);
    }

    @GetMapping
    public List<Chat> findAllChatsUser(@RequestHeader("Authorization") String jwtToken)
    {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return chatService.findUsersChat(userId);
    }
}
