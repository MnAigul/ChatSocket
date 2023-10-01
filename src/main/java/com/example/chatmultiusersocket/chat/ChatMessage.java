package com.example.chatmultiusersocket.chat;

import lombok.*;
import org.springframework.web.util.pattern.PathPattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;
    private String status;
}
