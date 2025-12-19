package com.jett.flowershop.presentation.response;

import com.jett.flowershop.domain.entity.ShopInfo;

import java.time.LocalDateTime;

public class ShopInfoResponse {
    private Long id;
    private String shopName;
    private String logoUrl;
    private String aboutUs;
    private String description;
    private String phone;
    private String email;
    private String address;
    private String workingHours;
    private String facebookUrl;
    private String instagramUrl;
    private LocalDateTime updatedAt;

    public ShopInfoResponse() {
    }

    public ShopInfoResponse(Long id, String shopName, String logoUrl, String aboutUs, 
                           String description, String phone, String email, String address, 
                           String workingHours, String facebookUrl, String instagramUrl, 
                           LocalDateTime updatedAt) {
        this.id = id;
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
        this.updatedAt = updatedAt;
    }

    public static ShopInfoResponse from(ShopInfo shopInfo) {
        return new ShopInfoResponse(
                shopInfo.getId(),
                shopInfo.getShopName(),
                shopInfo.getLogoUrl(),
                shopInfo.getAboutUs(),
                shopInfo.getDescription(),
                shopInfo.getPhone(),
                shopInfo.getEmail(),
                shopInfo.getAddress(),
                shopInfo.getWorkingHours(),
                shopInfo.getFacebookUrl(),
                shopInfo.getInstagramUrl(),
                shopInfo.getUpdatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
