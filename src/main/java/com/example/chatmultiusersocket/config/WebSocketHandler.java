// package com.example.chatmultiusersocket.config;

// import java.security.Principal;

// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.Payload;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.TextMessage;
// import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.socket.handler.TextWebSocketHandler;

// @Component
// public class WebSocketHandler extends TextWebSocketHandler {
//     // ...
//     @MessageMapping("/join")
//     public void joinRoom(@Payload String roomName, Principal principal) {
//         // Логика присоединения пользователя к комнате
//     }

//     @Override
//     protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//         // Обработка входящих сообщений в конкретной комнате
//     }
// }

