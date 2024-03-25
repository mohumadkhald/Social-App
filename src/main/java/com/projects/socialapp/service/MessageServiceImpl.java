package com.projects.socialapp.service;

import com.projects.socialapp.Repo.MessageRepo;
import com.projects.socialapp.Repo.UserRepo;
import com.projects.socialapp.mapper.MessageMapper;
import com.projects.socialapp.model.Message;
import com.projects.socialapp.requestDto.MessageRequestDto;
import com.projects.socialapp.responseDto.MessageResponseWithUserDto;
import com.projects.socialapp.traits.ApiTrait;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final ApiTrait apiTrait;
    private final UserRepo userRepo;
    private final MessageRepo messageRepo;
    private final MessageMapper messageMapper;
    private final ChatService chatService;

    @Override
    public MessageResponseWithUserDto createNewMessage(MessageRequestDto messageRequestDto) throws Exception {
        Integer chatId = messageRequestDto.getChatId();

        var chat = chatService.findChatById(chatId);
        // Assuming you have a method to extract participant IDs from the chat
        Integer id1 = chat.get(0).getId();
        Integer id2 = chat.get(1).getId();

        Integer senderId = messageRequestDto.getSender();

        // Check if the sender is one of the participants in the chat
        if (!senderId.equals(id1) && !senderId.equals(id2)) {
            throw new IllegalArgumentException("Sender is not a participant of the chat");
        }

        // Assuming you have a method to extract receiver ID from the message request
        Integer receiverId = messageRequestDto.getReceiver();

        var message = messageMapper.toMessage(messageRequestDto);
        var savedMessage = messageRepo.save(message);
        return messageMapper.toMessageDto(savedMessage);
    }


    public List<MessageResponseWithUserDto> findAllMessagesByChatId(Integer chatId, Integer userId) throws Exception {
        var chat = chatService.findChatById(chatId);
        // Assuming you have a method to extract participant IDs from the chat
        Integer id1 = chat.get(0).getId();
        Integer id2 = chat.get(1).getId();

        // Check if the sender is one of the participants in the chat
        if (!userId.equals(id1) && !userId.equals(id2)) {
            throw new IllegalArgumentException("Sender is not a participant of the chat");
        }
        List<Message> messages = messageRepo.findAllByChatId(chatId);
        return messages.stream()
                .map(messageMapper::toMessageDto)
                .collect(Collectors.toList());
    }



//    @Override
//    public String deleteMessage(Integer messageId, Integer userId)
//    {
//        // Retrieve the message by messageId
//        Message message = messageRepo.findById(messageId)
//                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + messageId));
//
//        // Check if the message belongs to the specified userId
//        if (!message.getUser().getId().equals(userId)) {
//            throw new NotAuthorizeException("You are not Authorized to Delete this Message");
//        }
//        // Delete the message
//        messageRepo.delete(message);
//
//        return "Message deleted successfully";
//    }
//
//
//    @Override
//    public List<MessageResponseWithUserDto> findMessagesByUserId(Integer userId) {
//        List<Message> messages = messageRepo.findAllByUserId(userId);
//        if (messages.isEmpty()) {
//            throw new MessageNotFoundException("No messages found for user with id: " + userId);
//        }
//
//        return getMessageResponseWithUserDto(messages);
//    }
//
//
//
//    @Override
//    public MessageResponseWithUserDto findMessageByMessageId(Integer messageId) {
//        Message message = messageRepo.findById(messageId)
//                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + messageId));
//
//        // Now fetch the associated user information
//        User user = message.getUser();
//
//        // Create a DTO that includes both message information and user's name
//        MessageResponseWithUserDto messageResponseWithUserDto = new MessageResponseWithUserDto();
//        messageResponseWithUserDto.setId(message.getId());
//        messageResponseWithUserDto.setCaption(message.getCaption());
//        messageResponseWithUserDto.setVideo(message.getVideo());
//        messageResponseWithUserDto.setImage(message.getImage());
//        messageResponseWithUserDto.setUserName(message.getUser().getFirstname());
//
//        return messageResponseWithUserDto;
//    }
//
//    @Override
//    public List<MessageResponseWithUserDto> findAllMessage() {
//        List<Message> messages = messageRepo.findAll();
//
//        return getMessageResponseWithUserDto(messages);
//    }
//
//    private List<MessageResponseWithUserDto> getMessageResponseWithUserDto(List<Message> messages) {
//        List<MessageResponseWithUserDto> messageResponseList = new ArrayList<>();
//        for (Message message : messages) {
//            MessageResponseWithUserDto messageResponseDto = new MessageResponseWithUserDto();
//            messageResponseDto.setId(message.getId());
//            messageResponseDto.setCaption(message.getCaption());
//            messageResponseDto.setVideo(message.getVideo());
//            messageResponseDto.setImage(message.getImage());
//            messageResponseDto.setUserName(message.getUser().getFirstname());
//            messageResponseList.add(messageResponseDto);
//        }
//
//        return messageResponseList;
//    }
//
//    @Override
//    public Message savedMessage(Integer messageId, Integer userId) {
//        return null;
//    }
//
//
//
//    @Override
//    public ResponseEntity<?> getUsersWhoLikedMessage(Integer messageId) {
//        // Retrieve the message by messageId
//        Message message = messageRepo.findById(messageId)
//                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + messageId));
//
//        // Retrieve the information of users who liked the message
//        List<Map<String, Object>> likedUsersInfo = message.getLikedByUsers().stream()
//                .map(user -> {
//                    Map<String, Object> userInfo = new HashMap<>();
//                    userInfo.put("id", user.getId());
//                    userInfo.put("fullName", user.getFirstname() + " " + user.getLastname());
//                    return userInfo;
//                })
//                .collect(Collectors.toList());
//
//        // Return the response
//        Map<String, Object> response = new HashMap<>();
//        response.put("likedUsers", likedUsersInfo);
//        response.put("likeCount", likedUsersInfo.size()); // Like count
//        return ResponseEntity.ok().body(response);
//    }
//
//    @Override
//    public ResponseEntity<?> likeUnlikeMessage(Integer messageId, Integer userId) {
//        // Retrieve the message by messageId
//        Message message = messageRepo.findById(messageId)
//                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + messageId));
//
//        // Retrieve the user by userId
//        User user = userRepo.findById(userId)
//                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
//
//        // Check if the user has already liked the message
//        if (message.getLikedByUsers().contains(user)) {
//            // User has liked the message, so unlike it
//            message.getLikedByUsers().remove(user);
//
//            messageRepo.save(message);
//            // Return success response for unlike
//            return ResponseEntity.ok().body("Message unliked successfully");
//        } else {
//            // User has not liked the message, so like it
//            message.getLikedByUsers().add(user);
//
//            messageRepo.save(message);
//            // Return success response for like
//            return ResponseEntity.ok().body("Message liked successfully");
//        }
//    }
//
//    @Override
//    public List<MessageResponseWithUserDto> findMessageByCaption(String caption)
//    {
//        List<Message> messages = messageRepo.findAllByCaption(caption);
//        if (messages.isEmpty()) {
//            throw new MessageNotFoundException("No messages found for user with this name: " + caption);
//        }
//
//        return getMessageResponseWithUserDto(messages);
//    }
//


}
