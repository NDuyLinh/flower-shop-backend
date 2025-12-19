package com.jett.flowershop.presentation.response;

import com.jett.flowershop.domain.entity.Review;

import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private String customerName;
    private String customerEmail;
    private String customerAvatar;
    private Integer rating;
    private String comment;
    private Long flowerId;
    private boolean isFeatured;
    private boolean isApproved;
    private String status;
    private LocalDateTime createdAt;

    public ReviewResponse() {
    }

    public ReviewResponse(Long id, String customerName, String customerEmail, 
                         String customerAvatar, Integer rating, String comment, 
                         Long flowerId, boolean isFeatured, boolean isApproved, 
                         String status, LocalDateTime createdAt) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerAvatar = customerAvatar;
        this.rating = rating;
        this.comment = comment;
        this.flowerId = flowerId;
        this.isFeatured = isFeatured;
        this.isApproved = isApproved;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getCustomerName(),
                review.getCustomerEmail(),
                review.getCustomerAvatar(),
                review.getRating(),
                review.getComment(),
                review.getFlowerId(),
                review.isFeatured(),
                review.isApproved(),
                review.getStatus(),
                review.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAvatar() {
        return customerAvatar;
    }

    public void setCustomerAvatar(String customerAvatar) {
        this.customerAvatar = customerAvatar;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getFlowerId() {
        return flowerId;
    }

    public void setFlowerId(Long flowerId) {
        this.flowerId = flowerId;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
