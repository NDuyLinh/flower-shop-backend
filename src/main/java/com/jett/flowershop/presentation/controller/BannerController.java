package com.jett.flowershop.presentation.controller;

import com.jett.flowershop.application.service.BannerService;
import com.jett.flowershop.domain.entity.Banner;
import com.jett.flowershop.presentation.request.BaseRequest;
import com.jett.flowershop.presentation.request.CreateBannerRequest;
import com.jett.flowershop.presentation.request.UpdateBannerRequest;
import com.jett.flowershop.presentation.response.BaseResponse;
import com.jett.flowershop.presentation.response.BannerResponse;
import com.jett.flowershop.presentation.response.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for Banner API.
 */
@Tag(name = "Banner", description = "Banner management APIs")
@RestController
@RequestMapping("/api/banners")
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @Operation(summary = "Create a new banner")
    @PostMapping
    public ResponseEntity<BaseResponse<BannerResponse>> createBanner(
            @Valid @RequestBody BaseRequest<CreateBannerRequest> request) {
        
        CreateBannerRequest createRequest = request.getRequestParameter();
        
        Banner banner = new Banner();
        banner.setTitle(createRequest.getTitle());
        banner.setSubtitle(createRequest.getSubtitle());
        banner.setDescription(createRequest.getDescription());
        banner.setImageUrl(createRequest.getImageUrl());
        banner.setLinkUrl(createRequest.getLinkUrl());
        banner.setBannerType(createRequest.getBannerType());
        banner.setDisplayOrder(createRequest.getDisplayOrder());
        banner.setIsActive(createRequest.getIsActive());
        banner.setStartDate(createRequest.getStartDate());
        banner.setEndDate(createRequest.getEndDate());

        Banner created = bannerService.createBanner(banner);

        BannerResponse response = mapToResponse(created);
        ResponseStatus status = new ResponseStatus("00", "Success");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new BaseResponse<>(LocalDateTime.now(), status, response));
    }

    @Operation(summary = "Get all banners")
    @GetMapping
    public ResponseEntity<BaseResponse<List<BannerResponse>>> getAllBanners() {
        
        List<Banner> banners = bannerService.getAllBanners();
        List<BannerResponse> responses = banners.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        ResponseStatus status = new ResponseStatus("00", "Success");
        return ResponseEntity.ok(new BaseResponse<>(LocalDateTime.now(), status, responses));
    }

    @Operation(summary = "Get active banners")
    @GetMapping("/active")
    public ResponseEntity<BaseResponse<List<BannerResponse>>> getActiveBanners() {
        
        List<Banner> banners = bannerService.getActiveBanners();
        List<BannerResponse> responses = banners.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        ResponseStatus status = new ResponseStatus("00", "Success");
        return ResponseEntity.ok(new BaseResponse<>(LocalDateTime.now(), status, responses));
    }

    @Operation(summary = "Get banner by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<BannerResponse>> getBannerById(@PathVariable Long id) {
        
        Optional<Banner> bannerOpt = bannerService.getBannerById(id);
        if (bannerOpt.isEmpty()) {
            ResponseStatus status = new ResponseStatus("03", "Banner not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(LocalDateTime.now(), status, null));
        }

        BannerResponse response = mapToResponse(bannerOpt.get());
        ResponseStatus status = new ResponseStatus("00", "Success");
        return ResponseEntity.ok(new BaseResponse<>(LocalDateTime.now(), status, response));
    }

    @Operation(summary = "Update banner")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<BannerResponse>> updateBanner(
            @PathVariable Long id,
            @Valid @RequestBody BaseRequest<UpdateBannerRequest> request) {
        
        try {
            UpdateBannerRequest updateRequest = request.getRequestParameter();
            
            Banner banner = new Banner();
            banner.setTitle(updateRequest.getTitle());
            banner.setSubtitle(updateRequest.getSubtitle());
            banner.setDescription(updateRequest.getDescription());
            banner.setImageUrl(updateRequest.getImageUrl());
            banner.setLinkUrl(updateRequest.getLinkUrl());
            banner.setBannerType(updateRequest.getBannerType());
            banner.setDisplayOrder(updateRequest.getDisplayOrder());
            banner.setIsActive(updateRequest.getIsActive());
            banner.setStartDate(updateRequest.getStartDate());
            banner.setEndDate(updateRequest.getEndDate());

            Banner updated = bannerService.updateBanner(id, banner);
            BannerResponse response = mapToResponse(updated);
            ResponseStatus status = new ResponseStatus("00", "Success");
            
            return ResponseEntity.ok(new BaseResponse<>(LocalDateTime.now(), status, response));
        } catch (RuntimeException e) {
            ResponseStatus status = new ResponseStatus("03", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(LocalDateTime.now(), status, null));
        }
    }

    @Operation(summary = "Delete banner")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteBanner(@PathVariable Long id) {
        
        try {
            bannerService.deleteBanner(id);
            ResponseStatus status = new ResponseStatus("00", "Banner deleted successfully");
            return ResponseEntity.ok(new BaseResponse<>(LocalDateTime.now(), status, null));
        } catch (RuntimeException e) {
            ResponseStatus status = new ResponseStatus("03", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new BaseResponse<>(LocalDateTime.now(), status, null));
        }
    }

    private BannerResponse mapToResponse(Banner banner) {
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
}
