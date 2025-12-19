package com.jett.flowershop.presentation.controller;

import com.jett.flowershop.application.service.*;
import com.jett.flowershop.domain.entity.*;
import com.jett.flowershop.presentation.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/homepage")
@Tag(name = "Homepage", description = "Homepage aggregated data APIs")
public class HomepageController {
    
    private final BannerService bannerService;
    private final CategoryService categoryService;
    private final FlowerService flowerService;
    private final ReviewService reviewService;
    private final ShopInfoService shopInfoService;

    public HomepageController(BannerService bannerService,
                             CategoryService categoryService,
                             FlowerService flowerService,
                             ReviewService reviewService,
                             ShopInfoService shopInfoService) {
        this.bannerService = bannerService;
        this.categoryService = categoryService;
        this.flowerService = flowerService;
        this.reviewService = reviewService;
        this.shopInfoService = shopInfoService;
    }

    @GetMapping
    @Operation(
        summary = "Get homepage data",
        description = "Retrieve all data needed for the homepage: active banners, featured categories, hot flowers, best-selling flowers, featured reviews, and shop info"
    )
    public ResponseEntity<BaseResponse<HomepageResponse>> getHomepageData() {
        // Get active banners
        List<BannerResponse> activeBanners = bannerService.getActiveBanners().stream()
                .map(BannerResponse::from)
                .collect(Collectors.toList());

        // Get featured categories
        List<CategoryResponse> featuredCategories = categoryService.getFeaturedCategories().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());

        // Get hot flowers
        List<FlowerResponse> hotFlowers = flowerService.getHotFlowers().stream()
                .map(FlowerResponse::from)
                .collect(Collectors.toList());

        // Get best-selling flowers (limit to 8)
        List<FlowerResponse> bestSellingFlowers = flowerService.getBestSellingFlowers(8).stream()
                .map(FlowerResponse::from)
                .collect(Collectors.toList());

        // Get featured reviews
        List<ReviewResponse> featuredReviews = reviewService.getFeaturedReviews().stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());

        // Get shop info
        ShopInfo shopInfo = shopInfoService.getShopInfo();
        ShopInfoResponse shopInfoResponse = ShopInfoResponse.from(shopInfo);

        // Build homepage response
        HomepageResponse homepageResponse = new HomepageResponse(
                activeBanners,
                featuredCategories,
                hotFlowers,
                bestSellingFlowers,
                featuredReviews,
                shopInfoResponse
        );

        return ResponseEntity.ok(BaseResponse.success(homepageResponse, "Homepage data retrieved successfully"));
    }
}
