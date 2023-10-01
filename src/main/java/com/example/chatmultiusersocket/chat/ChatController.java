package com.example.chatmultiusersocket.chat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class ChatController {
    private Set<ChatRoom> chatRooms = new HashSet<>();

    @MessageMapping("/chat.sendMessage/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String chatRoom) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor,
            @DestinationVariable String chatRoom) {
        String username = chatMessage.getSender();
        headerAccessor.getSessionAttributes().put("username", username);

        // Create or retrieve the chat room
        if (!chatRooms.contains(chatRoom)) {
            ChatRoom room = new ChatRoom(chatRoom,new HashMap<String, User>(),username,new ArrayList<>());
            room.addUser(new User(username, "ACTIVE"));
            chatRooms.add(room);
        } else {

        }

        // ChatRoom room = chatRooms.computeIfAbsent(chatRoom, k -> new
        // ChatRoom(username));
        //
        return chatMessage;
    }

    // NOT WORKING
    @MessageMapping("/chat.updateStatus/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public ChatMessage updateStatus(@Payload ChatMessage chatMessage, @DestinationVariable String chatRoom) {
        String sender = chatMessage.getSender();
        String newStatus = chatMessage.getStatus();

        // Update the user's status in the chat room

        // if (chatRooms.contains(chatRoom)) {
        // ChatRoom room = chatRooms.get(chatRoom);
        // ChatRoom room = chatRooms.;
        // if (room.getUsers().containsKey(sender)) {
        // room.getUsers().get(sender).setStatus(newStatus);
        // }
        // }

        return chatMessage;
    }

    @RequestMapping("/chatrooms") // TODO:
    public Set<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    @RequestMapping("/chatrooms/messages/{chatRoom}")
    public List<ChatMessage> getMessages(@DestinationVariable String chatRoom) {
        List<ChatMessage> result = new ArrayList<>();
        for (ChatRoom chat : chatRooms) {
            if (chat.getName().equals(chatRoom)) {
                result = chat.getMessages();
            }
        }
        return result;

    }
}
