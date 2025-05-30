package com.example.my_computer.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role role) {
        return role == null ? null : role.name();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return Role.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown role value from DB: " + dbData, e);
        }
    }
}
