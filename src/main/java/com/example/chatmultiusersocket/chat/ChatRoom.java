package com.example.chatmultiusersocket.chat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    private Map<String, User> users; // Map of username to User object
    private String admin;
    private List<ChatMessage> messages;

    public ChatRoom(String admin) {
        this.users = new HashMap<>();
        this.admin = admin;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }
}
