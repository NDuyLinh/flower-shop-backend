package com.jett.flowershop.presentation.request;

import jakarta.validation.constraints.*;

public class CreateReviewRequest {
    
    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name must not exceed 100 characters")
    private String customerName;
    
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;
    
    private String customerAvatar;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;
    
    @NotBlank(message = "Comment is required")
    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String comment;
    
    private Long flowerId;

    public CreateReviewRequest() {
    }

    public CreateReviewRequest(String customerName, String customerEmail, String customerAvatar, 
                              Integer rating, String comment, Long flowerId) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerAvatar = customerAvatar;
        this.rating = rating;
        this.comment = comment;
        this.flowerId = flowerId;
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
}
