package com.jett.flowershop.application.dto;

/**
 * DTO for image upload response
 */
public class ImageUploadResult {
    private String originalUrl;
    private String thumbUrl;
    private String mediumUrl;
    private String largeUrl;

    public ImageUploadResult() {
    }

    public ImageUploadResult(String originalUrl, String thumbUrl, String mediumUrl, String largeUrl) {
        this.originalUrl = originalUrl;
        this.thumbUrl = thumbUrl;
        this.mediumUrl = mediumUrl;
        this.largeUrl = largeUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public String getLargeUrl() {
        return largeUrl;
    }

    public void setLargeUrl(String largeUrl) {
        this.largeUrl = largeUrl;
    }
}
