package com.jett.flowershop.presentation.response;

import java.util.List;

public class HomepageResponse {
    private List<BannerResponse> activeBanners;
    private List<CategoryResponse> featuredCategories;
    private List<FlowerResponse> hotFlowers;
    private List<FlowerResponse> bestSellingFlowers;
    private List<ReviewResponse> featuredReviews;
    private ShopInfoResponse shopInfo;

    public HomepageResponse() {
    }

    public HomepageResponse(List<BannerResponse> activeBanners, 
                           List<CategoryResponse> featuredCategories,
                           List<FlowerResponse> hotFlowers,
                           List<FlowerResponse> bestSellingFlowers,
                           List<ReviewResponse> featuredReviews,
                           ShopInfoResponse shopInfo) {
        this.activeBanners = activeBanners;
        this.featuredCategories = featuredCategories;
        this.hotFlowers = hotFlowers;
        this.bestSellingFlowers = bestSellingFlowers;
        this.featuredReviews = featuredReviews;
        this.shopInfo = shopInfo;
    }

    public List<BannerResponse> getActiveBanners() {
        return activeBanners;
    }

    public void setActiveBanners(List<BannerResponse> activeBanners) {
        this.activeBanners = activeBanners;
    }

    public List<CategoryResponse> getFeaturedCategories() {
        return featuredCategories;
    }

    public void setFeaturedCategories(List<CategoryResponse> featuredCategories) {
        this.featuredCategories = featuredCategories;
    }

    public List<FlowerResponse> getHotFlowers() {
        return hotFlowers;
    }

    public void setHotFlowers(List<FlowerResponse> hotFlowers) {
        this.hotFlowers = hotFlowers;
    }

    public List<FlowerResponse> getBestSellingFlowers() {
        return bestSellingFlowers;
    }

    public void setBestSellingFlowers(List<FlowerResponse> bestSellingFlowers) {
        this.bestSellingFlowers = bestSellingFlowers;
    }

    public List<ReviewResponse> getFeaturedReviews() {
        return featuredReviews;
    }

    public void setFeaturedReviews(List<ReviewResponse> featuredReviews) {
        this.featuredReviews = featuredReviews;
    }

    public ShopInfoResponse getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfoResponse shopInfo) {
        this.shopInfo = shopInfo;
    }
}
