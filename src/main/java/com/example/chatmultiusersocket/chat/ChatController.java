package com.example.chatmultiusersocket.chat;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.example.chatmultiusersocket.chat.entity.MessageEntity;

@Controller
public class ChatController {


    // public Message<?> preSend(Message<?> message, MessageChannel channel) {
    //     StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    //     if (StompCommand.CONNECT.equals(accessor.getCommand())) {
    //         String username = accessor.getFirstNativeHeader("username");
    //         String chatRoom = accessor.getFirstNativeHeader("chatRoom");
    //         accessor.getSessionAttributes().put("username", username);
    //         accessor.getSessionAttributes().put("chatRoom", chatRoom);
    //     }

    //     return message;
    // }

    @MessageMapping("/chat.sendMessage/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public MessageEntity sendMessage(@Payload MessageEntity chatMessage, @DestinationVariable String chatRoom) {

        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public MessageEntity addUser(@Payload MessageEntity chatMessage, SimpMessageHeaderAccessor headerAccessor,
            @DestinationVariable String chatRoom) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getFromUserId());
        return chatMessage;
    }
}
