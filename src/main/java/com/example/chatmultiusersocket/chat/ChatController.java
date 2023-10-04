package com.example.chatmultiusersocket.chat;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class ChatController {
    private Map<String, ChatRoom> chatRooms = new HashMap<>();

    @MessageMapping("/chat.sendMessage/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String chatRoom) {
        chatRooms.get(chatRoom).addMessage(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor,
            @DestinationVariable String chatRoom) {
        String username = chatMessage.getSender();
        headerAccessor.getSessionAttributes().put("username", username);
        if (!chatRooms.containsKey(chatRoom)) {
            ChatRoom room = new ChatRoom(new HashMap<String, User>(), username, new ArrayList<>());
            room.addUser(new User(username, "ACTIVE"));
            chatRooms.put(chatRoom, room);
        } else {
            if(!chatRooms.get(chatRoom).getUsers().containsKey(username)) {
                 chatRooms.get(chatRoom).addUser(new User(username, "ACTIVE"));
            }
        }
        return chatMessage;
    }

    // NOT WORKING
    @MessageMapping("/chat.updateStatus/{chatRoom}")
    @SendTo("/topic/{chatRoom}")
    public ChatMessage updateStatus(@Payload ChatMessage chatMessage, @DestinationVariable String chatRoom) {
        String sender = chatMessage.getSender();
        if (chatRooms.get(chatRoom).getUsers().get(sender).getStatus().equalsIgnoreCase("BLOCKED")) {
            chatRooms.get(chatRoom).getUsers().get(sender).setStatus("ACTIVE");
        } else{
            chatRooms.get(chatRoom).getUsers().get(sender).setStatus("BLOCKED");
        }
        return chatMessage;
    }

    @RequestMapping("/chatrooms") // TODO:
    public Set<String> getChatRooms() {
        return chatRooms.keySet();
    }

    @RequestMapping("/chatrooms/messages/{chatRoom}")
    public List<ChatMessage> getMessages(@PathVariable("chatRoom") String chatRoom) {
        if (chatRooms.containsKey(chatRoom)) {
            return chatRooms.get(chatRoom).getMessages();
        }
        return Collections.emptyList();
    }

    @PostMapping("/chatrooms/messages/add/{chatRoom}")
    public ChatMessage addMessageToChatRoom(@PathVariable("chatRoom") String chatRoom,
            @Payload ChatMessage chatMessage) {
        chatRooms.get(chatRoom).addMessage(chatMessage);
        return chatMessage;
    }

    @RequestMapping("/chatrooms/getUserStatus/{chatRoom}/{username}")
    public String getUserStatus(@PathVariable("username") String username , @PathVariable("chatRoom") String chatRoom) {
        if (!chatRooms.containsKey(chatRoom)) {
            return null;
        }
        return chatRooms.get(chatRoom).getUsers().get(username).getStatus();
    }
}
