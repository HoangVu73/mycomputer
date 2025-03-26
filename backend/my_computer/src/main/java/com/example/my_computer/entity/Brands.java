package com.example.my_computer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "brands", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Brands {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    public Brands() {
    }

    public Brands(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters và Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
