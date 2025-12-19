package com.jett.flowershop.presentation.response;

import java.math.BigDecimal;

/**
 * Response object for Flower API.
 *
 * Fields:
 * - id
 * - name
 * - price
 * - description
 * - occasion
 * - color
 */
public class FlowerResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
    private String occasion;
    private String color;

    public FlowerResponse() {
    }

    public FlowerResponse(Long id, String name, BigDecimal price, String description, String occasion, String color) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.occasion = occasion;
        this.color = color;
    }

    public static FlowerResponse from(com.jett.flowershop.domain.entity.Flower flower) {
        return new FlowerResponse(
                flower.getId(),
                flower.getName(),
                flower.getPrice(),
                flower.getDescription(),
                flower.getOccasion(),
                flower.getColor()
        );
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
}
