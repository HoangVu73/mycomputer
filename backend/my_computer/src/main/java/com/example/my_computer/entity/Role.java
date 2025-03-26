package com.example.my_computer.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN,
    CUSTOMER;

    @JsonCreator
    public static Role forValue(String value) {
        return value == null ? null : Role.valueOf(value.toUpperCase());
    }
}
