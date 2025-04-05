package com.projects.socialapp.controller;

import com.projects.socialapp.requestDto.MessageRequestDto;
import com.projects.socialapp.responseDto.MessageResponseWithUserDto;
import com.projects.socialapp.service.ChatService;
import com.projects.socialapp.service.FileStorageService;
import com.projects.socialapp.service.MessageService;
import com.projects.socialapp.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;
    private final ChatService chatService;
    private final FileStorageService fileStorageService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/{chatId}")
    public MessageResponseWithUserDto createMessage(@PathVariable Integer chatId,
                                                    @Valid MessageRequestDto messageRequestDto,
                                                    @RequestPart(value = "image", required = false) MultipartFile image,
                                                    @RequestHeader("Authorization") String jwtToken
    ) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        var chat = chatService.findChatById(chatId);

        // Assuming you have a method to extract receiver ID from the chat
        Integer receiverId;
        if (!Objects.equals(chat.get(0).getId(), userId)) {
            receiverId = chat.get(0).getId();
        } else {
            receiverId = chat.get(1).getId();
        }


        if (image != null)
        {
            String imageUrl = fileStorageService.storeFile(image, userId + "/messages");
            messageRequestDto.setImg(imageUrl);
        }




        messageRequestDto.setSender(userId);
        messageRequestDto.setChatId(chatId);
        messageRequestDto.setReceiver(receiverId);

        // Create a new message
        MessageResponseWithUserDto responseDto = messageService.createNewMessage(messageRequestDto);

        // Notify WebSocket clients about the new message
//        messagingTemplate.convertAndSend("/topic/chat/" + chatId, responseDto);

        return responseDto;
    }



    @GetMapping("/{chatId}")
    public List<MessageResponseWithUserDto>
    getMessageDetails(@PathVariable Integer chatId,
                      @RequestHeader("Authorization") String jwtToken) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return messageService.findAllMessagesByChatId(chatId, userId);
    }

    @GetMapping("/{chatId}/last")
    public MessageResponseWithUserDto getLastMessage(
            @PathVariable Integer chatId,
            @RequestHeader("Authorization") String jwtToken) throws Exception {

        Integer userId = userService.findUserIdByJwt(jwtToken);
        return messageService.findLastMessageByChatId(chatId, userId);

    }

}




