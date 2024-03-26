package com.projects.socialapp.controller;

import com.projects.socialapp.requestDto.MessageRequestDto;
import com.projects.socialapp.responseDto.MessageResponseWithUserDto;
import com.projects.socialapp.service.ChatService;
import com.projects.socialapp.service.FileStorageService;
import com.projects.socialapp.service.MessageService;
import com.projects.socialapp.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @PostMapping("/{chatId}")
    public MessageResponseWithUserDto createMessage(@PathVariable Integer chatId,
                                                    @Valid  MessageRequestDto messageRequestDto,
                                                    @RequestPart("image") MultipartFile image,
                                                    @RequestHeader("Authorization") String jwtToken) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        var chat = chatService.findChatById(chatId);

        // Assuming you have a method to extract receiver ID from the chat
        Integer receiverId;
        if (!Objects.equals(chat.get(0).getId(), userId)) {
            receiverId = chat.get(0).getId();
        } else {
            receiverId = chat.get(1).getId();
        }

        messageRequestDto.setSender(userId);
        messageRequestDto.setChatId(chatId);
        messageRequestDto.setReceiver(receiverId);

        // Create a new message
        MessageResponseWithUserDto responseDto = messageService.createNewMessage(messageRequestDto);

        // Now, handle the image upload
        if (!image.isEmpty()) {
            // Process the image upload here (save it to the file system or database)
            // You can use the imageService or any other service to handle image upload
            // For demonstration, let's assume we save the image to the file system
            String imageUrl = fileStorageService.storeFile(image, userId + "/messages");
            // You may want to associate this image URL with the message in the database
            // Update the response DTO with the image URL
            responseDto.setImage(imageUrl);
        }

        return responseDto;
    }

    @GetMapping("/{chatId}")
    public List<MessageResponseWithUserDto>
    getMessageDetails(@PathVariable Integer chatId,
                      @RequestHeader("Authorization") String jwtToken) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return messageService.findAllMessagesByChatId(chatId, userId);
    }

//    @GetMapping("/{messageId}")
//    public MessageResponseWithUserDto getMessageDetails(@PathVariable Integer messageId) {
//        return messageService.findMessageByMessageId(messageId);
//    }


    @DeleteMapping("{chatId}/{msgID}")
    public String
    deleteMsg(@PathVariable Integer msgID,
              @RequestHeader("Authorization") String jwtToken, @PathVariable Integer chatId) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return messageService.deleteMessage(msgID, userId);
    }

    @DeleteMapping("{chatId}")
    public String
    deleteAllMsgByChatId(
              @RequestHeader("Authorization") String jwtToken, @PathVariable Integer chatId) throws Exception {
        Integer userId = userService.findUserIdByJwt(jwtToken);
        return messageService.deleteAllMessageByChatId(userId, chatId);
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String folder = "test"; // Specify the folder path where you want to upload the file
            String fileName = fileStorageService.storeFile(file, folder);
            return ResponseEntity.ok().body("File uploaded successfully: " + fileName);
        } catch (IOException ex) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + ex.getMessage());
        }
    }

}
