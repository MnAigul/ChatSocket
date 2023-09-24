package com.example.chatmultiusersocket.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat.sendMessage/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String chatRoom) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor,
            @DestinationVariable String chatRoom) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
