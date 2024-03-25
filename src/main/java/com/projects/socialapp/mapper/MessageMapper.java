package com.projects.socialapp.mapper;

import com.projects.socialapp.Repo.ChatRepo;
import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.model.Chat;
import com.projects.socialapp.model.Message;
import com.projects.socialapp.model.User;
import com.projects.socialapp.requestDto.MessageRequestDto;
import com.projects.socialapp.responseDto.MessageResponseWithUserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class MessageMapper {
    private final ChatRepo chatRepo;
    private final UserRepo userRepo;
    public Message toMessage(MessageRequestDto messageRequestDto) {
        // Fetch existing Chat entity from the database based on the provided ID
        Chat chat = chatRepo.findById(messageRequestDto.getChatId())
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        // Fetch existing Sender entity from the database based on the provided ID
        User sender = userRepo.findById(messageRequestDto.getSender())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));

        // Fetch existing Receiver entity from the database based on the provided ID
        User receiver = userRepo.findById(messageRequestDto.getReceiver())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        // Create the Message object and set its properties
        Message message = new Message();
        message.setContent(messageRequestDto.getContent());
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setChat(chat);

        return message;
    }


public MessageResponseWithUserDto toMessageDto(Message message) {
    // Ensure that sender is not null before accessing its properties
    if (message.getSender() != null) {
        // Access sender's firstname property
        String senderFirstname = message.getSender().getFirstname()+" "+message.getSender().getLastname();
        String reciverUsername = message.getReceiver().getFirstname()+" "+message.getReceiver().getLastname();
        LocalDateTime createdAt = message.getCreatedAt();
        ZoneId egyptZone = ZoneId.of("Africa/Cairo");
        LocalDateTime createdAtInEgypt = createdAt.atZone(ZoneOffset.UTC).withZoneSameInstant(egyptZone).toLocalDateTime();

        // Format the createdAt time to display
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCreatedAt = createdAtInEgypt.format(formatter);

        // Construct the DTO with the sender's firstname
        return new MessageResponseWithUserDto(message.getId(), message.getContent(),senderFirstname, reciverUsername, formattedCreatedAt);
    } else {
        // Handle the case where sender is null
        // You can throw an exception, return a default value, or handle it based on your application's requirements
        // For demonstration purposes, we'll return null here
        return null;
    }
}
}
