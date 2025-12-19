package com.jett.flowershop.presentation.response;

import java.time.LocalDateTime;

/**
 * Response object for Banner API.
 */
public class BannerResponse {

    private Long id;
    private String title;
    private String subtitle;
    private String description;
    private String imageUrl;
    private String linkUrl;
    private String bannerType;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public BannerResponse() {
    }

    public BannerResponse(Long id, String title, String subtitle, String description, 
                         String imageUrl, String linkUrl, String bannerType, 
                         Integer displayOrder, Boolean isActive, 
                         LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.bannerType = bannerType;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static BannerResponse from(com.jett.flowershop.domain.entity.Banner banner) {
        return new BannerResponse(
                banner.getId(),
                banner.getTitle(),
                banner.getSubtitle(),
                banner.getDescription(),
                banner.getImageUrl(),
                banner.getLinkUrl(),
                banner.getBannerType(),
                banner.getDisplayOrder(),
                banner.getIsActive(),
                banner.getStartDate(),
                banner.getEndDate()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
