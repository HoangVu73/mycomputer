package com.example.my_computer.dto;

public class LoginDTO {
    private String account; // Có thể là username hoặc email
    private String password;

    // Getters & Setters
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
