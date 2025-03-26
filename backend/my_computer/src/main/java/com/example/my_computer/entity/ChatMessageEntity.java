package com.example.my_computer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên người gửi tin nhắn
    @Column(nullable = false)
    private String sender;

    // Nội dung tin nhắn
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Loại tin nhắn: CHAT, JOIN, LEAVE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMessageType type;

    // Target: chỉ định user nhận khi admin gửi tin nhắn riêng cho user
    private String target;

    // Thời gian tin nhắn được gửi
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public enum ChatMessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public ChatMessageEntity() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessageEntity(String sender, String content, ChatMessageType type, String target) {
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.target = target;
        this.timestamp = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public ChatMessageType getType() {
        return type;
    }
    public void setType(ChatMessageType type) {
        this.type = type;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
