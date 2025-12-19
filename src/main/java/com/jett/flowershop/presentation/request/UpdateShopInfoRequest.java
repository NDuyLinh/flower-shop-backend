package com.jett.flowershop.presentation.request;

import jakarta.validation.constraints.*;

public class UpdateShopInfoRequest {
    
    @NotBlank(message = "Shop name is required")
    @Size(max = 200, message = "Shop name must not exceed 200 characters")
    private String shopName;
    
    private String logoUrl;
    
    @Size(max = 500, message = "About us must not exceed 500 characters")
    private String aboutUs;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    private String phone;
    
    @Email(message = "Invalid email format")
    private String email;
    
    private String address;
    
    private String workingHours;
    
    private String facebookUrl;
    
    private String instagramUrl;

    public UpdateShopInfoRequest() {
    }

    public UpdateShopInfoRequest(String shopName, String logoUrl, String aboutUs, 
                                String description, String phone, String email, 
                                String address, String workingHours, 
                                String facebookUrl, String instagramUrl) {
        this.shopName = shopName;
        this.logoUrl = logoUrl;
        this.aboutUs = aboutUs;
        this.description = description;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.workingHours = workingHours;
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }
}
