package com.example.my_computer.dto;

import com.example.my_computer.entity.SaleStatus;
import java.math.BigDecimal;

public class ProductDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private SaleStatus saleStatus;
    private String image;
    private BigDecimal discount;
    // Các trường mới:
    private String cpu;
    private String ram;
    private String hardDrive;
    private Double averageRating; // Điểm trung bình từ Reviews

    public ProductDTO(Integer id, String name, String description, BigDecimal price, Integer quantity,
                      SaleStatus saleStatus, String image, BigDecimal discount,
                      String cpu, String ram, String hardDrive, Double averageRating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.saleStatus = saleStatus;
        this.image = image;
        this.discount = discount;
        this.cpu = cpu;
        this.ram = ram;
        this.hardDrive = hardDrive;
        this.averageRating = averageRating;
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
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public SaleStatus getSaleStatus() {
        return saleStatus;
    }
    public void setSaleStatus(SaleStatus saleStatus) {
        this.saleStatus = saleStatus;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public BigDecimal getDiscount() {
        return discount;
    }
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    public String getCpu() {
        return cpu;
    }
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }
    public String getRam() {
        return ram;
    }
    public void setRam(String ram) {
        this.ram = ram;
    }
    public String getHardDrive() {
        return hardDrive;
    }
    public void setHardDrive(String hardDrive) {
        this.hardDrive = hardDrive;
    }
    public Double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
