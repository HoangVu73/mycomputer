package com.example.my_computer.model;

import com.example.my_computer.entity.ChatMessageEntity;
import com.example.my_computer.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessageEntity save(ChatMessageEntity message) {
        return chatMessageRepository.save(message);
    }

    // Lấy tin nhắn mà username là người gửi hoặc là target
    public List<ChatMessageEntity> getChatHistory(String username) {
        return chatMessageRepository.findAllBySenderOrTarget(username, username);
    }
}
