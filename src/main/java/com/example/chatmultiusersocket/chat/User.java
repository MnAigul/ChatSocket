package com.example.chatmultiusersocket.chat;

public class User {
    private String username;
    private String status;

    public User(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

