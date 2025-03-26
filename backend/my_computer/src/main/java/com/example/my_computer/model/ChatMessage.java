package com.example.my_computer.model;

public class ChatMessage {
    private String sender;
    private String content;
    private MessageType type;
    // Trường target: dùng để chỉ định tên người nhận (đối với tin nhắn từ admin)
    private String target;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public ChatMessage() {
    }

    public ChatMessage(String sender, String content, MessageType type, String target) {
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.target = target;
    }

    // Getters & Setters
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
    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
}
