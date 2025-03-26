package com.example.my_computer.controller;

import com.example.my_computer.model.ChatMessage;
import com.example.my_computer.entity.ChatMessageEntity;
import com.example.my_computer.entity.ChatMessageEntity.ChatMessageType;
import com.example.my_computer.model.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PrivateChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageService chatMessageService;

    // WebSocket endpoint: Khi user gửi tin nhắn cho admin,
    // lưu vào DB và gửi tin nhắn lên topic chung /topic/admin
    @MessageMapping("/chat.userToAdmin")
    public void userToAdmin(ChatMessage chatMessage) {
        System.out.println("[Controller] User -> Admin: " + chatMessage.getSender() + " | " + chatMessage.getContent());
        // Chuyển đổi model sang entity (target = null vì user gửi cho admin)
        ChatMessageEntity entity = new ChatMessageEntity(
                chatMessage.getSender(),
                chatMessage.getContent(),
                ChatMessageType.CHAT,
                null
        );
        chatMessageService.save(entity);
        // Gửi tin nhắn tới topic chung
        messagingTemplate.convertAndSend("/topic/admin", chatMessage);
    }

    // WebSocket endpoint: Khi admin gửi tin nhắn cho user,
    // lưu vào DB và gửi tin nhắn đến topic riêng /topic/user.{target}
    @MessageMapping("/chat.adminToUser")
    public void adminToUser(ChatMessage chatMessage) {
        System.out.println("[Controller] Admin -> " + chatMessage.getTarget() + ": " + chatMessage.getContent());
        ChatMessageEntity entity = new ChatMessageEntity(
                chatMessage.getSender(),
                chatMessage.getContent(),
                ChatMessageType.CHAT,
                chatMessage.getTarget()
        );
        chatMessageService.save(entity);
        String userTopic = "/topic/user." + chatMessage.getTarget();
        messagingTemplate.convertAndSend(userTopic, chatMessage);
    }

    // REST endpoint: Lấy lịch sử tin nhắn của một user (bao gồm tin nhắn từ user gửi và admin gửi cho user)
    @GetMapping("/api/chat/history")
    public List<ChatMessageEntity> getChatHistory(@RequestParam("username") String username) {
        return chatMessageService.getChatHistory(username);
    }
}
