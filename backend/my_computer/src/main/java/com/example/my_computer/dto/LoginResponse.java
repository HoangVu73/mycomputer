package com.example.my_computer.dto;

import com.example.my_computer.entity.Users;

public class LoginResponse {
    private Users user;
    private String token;

    public LoginResponse(Users user, String token) {
        this.user = user;
        this.token = token;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
