package com.projects.socialapp.Config;

//@AllArgsConstructor
//public class WebSocketController {
//    private final SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/send/message/{chatId}")
//    public void sendMessageToChat(@PathVariable Integer chatId, @Payload MessageRequestDto messageRequestDto) {
//        // Handle the incoming message and process it
//        // You may want to save the message to the database
//
//        // Broadcast the message to WebSocket subscribers of the chat
//        messagingTemplate.convertAndSend("/topic/chat/" + chatId, messageRequestDto);
//    }
//}


import com.projects.socialapp.requestDto.MessageRequestDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/send/message/{chatId}")
    public void sendMessageToChat(@PathVariable Integer chatId, @Payload MessageRequestDto messageRequestDto) {
        // Handle the incoming message and process it
        // You may want to save the message to the database

        // Broadcast the message to WebSocket subscribers of the chat
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, messageRequestDto);
    }
}





//public class MyWebSocketHandler extends TextWebSocketHandler {
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // Handle incoming WebSocket messages
//        String payload = message.getPayload();
//        System.out.println("Received message: " + payload);
//        session.sendMessage(new TextMessage("Response message"));
//    }
//}


