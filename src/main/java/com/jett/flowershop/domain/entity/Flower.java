package com.jett.flowershop.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain entity representing a Flower product in the flower shop.
 *
 * Rules:
 * - No framework annotations
 * - Pure business object
 * - Fields: id, name, price, description, occasion, color, imageUrl, stockQuantity,
 *           isFeatured, isHot, viewCount, soldCount, status, categoryId, createdAt, updatedAt
 */
public class Flower {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String occasion;
    private String color;
    private String imageUrl;
    private Integer stockQuantity;
    private Boolean isFeatured;
    private Boolean isHot;
    private Integer viewCount;
    private Integer soldCount;
    private String status; // ACTIVE, INACTIVE, OUT_OF_STOCK
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Flower() {
        this.stockQuantity = 0;
        this.isFeatured = false;
        this.isHot = false;
        this.viewCount = 0;
        this.soldCount = 0;
        this.status = "ACTIVE";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public boolean isFeatured() {
        return Boolean.TRUE.equals(isFeatured);
    }

    public Boolean getIsHot() {
        return isHot;
    }

    public void setIsHot(Boolean isHot) {
        this.isHot = isHot;
    }

    public boolean isHot() {
        return Boolean.TRUE.equals(isHot);
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(Integer soldCount) {
        this.soldCount = soldCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flower flower = (Flower) o;
        return Objects.equals(id, flower.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Flower{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", occasion='" + occasion + '\'' +
                ", color='" + color + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
