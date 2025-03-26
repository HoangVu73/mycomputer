package com.example.my_computer.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Tên sản phẩm
    @Column(name = "name", nullable = false)
    private String name;

    // Mô tả sản phẩm
    @Column(columnDefinition = "text")
    private String description;

    // Giá sản phẩm
    private BigDecimal price;

    // Số lượng sản phẩm
    private Integer quantity;

    // Trạng thái bán
    @Enumerated(EnumType.STRING)
    @Column(name = "sale_status")
    private SaleStatus saleStatus;

    // Ảnh sản phẩm (URL hoặc chuỗi Base64)
    private String image;

    // Giảm giá
    private BigDecimal discount;

    // ID của thương hiệu
    @Column(name = "brand_id")
    private Integer brandId;

    // Thời gian tạo và cập nhật
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Các trường mới: CPU, RAM, Hard Drive
    @Column(name = "cpu")
    private String cpu;

    @Column(name = "ram")
    private String ram;

    @Column(name = "hard_drive")
    private String hardDrive;

    // Constructors
    public Product() {
    }

    public Product(String name, String description, BigDecimal price, Integer quantity,
                   SaleStatus saleStatus, String image, BigDecimal discount, Integer brandId,
                   String cpu, String ram, String hardDrive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.saleStatus = saleStatus;
        this.image = image;
        this.discount = discount;
        this.brandId = brandId;
        this.cpu = cpu;
        this.ram = ram;
        this.hardDrive = hardDrive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    public Integer getBrandId() {
        return brandId;
    }
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
}
