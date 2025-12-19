package com.jett.flowershop.presentation.controller;

import com.jett.flowershop.application.service.ShopInfoService;
import com.jett.flowershop.domain.entity.ShopInfo;
import com.jett.flowershop.presentation.request.UpdateShopInfoRequest;
import com.jett.flowershop.presentation.request.BaseRequest;
import com.jett.flowershop.presentation.response.BaseResponse;
import com.jett.flowershop.presentation.response.ShopInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shop-info")
@Tag(name = "Shop Info", description = "Shop information management APIs")
public class ShopInfoController {
    
    private final ShopInfoService shopInfoService;

    public ShopInfoController(ShopInfoService shopInfoService) {
        this.shopInfoService = shopInfoService;
    }

    @GetMapping
    @Operation(summary = "Get shop information", description = "Retrieve current shop information")
    public ResponseEntity<BaseResponse<ShopInfoResponse>> getShopInfo() {
        ShopInfo shopInfo = shopInfoService.getShopInfo();
        ShopInfoResponse response = ShopInfoResponse.from(shopInfo);
        return ResponseEntity.ok(BaseResponse.success(response, "Shop information retrieved successfully"));
    }

    @PutMapping
    @Operation(summary = "Update shop information", description = "Update shop information")
    public ResponseEntity<BaseResponse<ShopInfoResponse>> updateShopInfo(
            @Valid @RequestBody BaseRequest<UpdateShopInfoRequest> request) {
        UpdateShopInfoRequest data = request.getData();
        ShopInfo shopInfo = shopInfoService.updateShopInfo(
                data.getShopName(),
                data.getLogoUrl(),
                data.getAboutUs(),
                data.getDescription(),
                data.getPhone(),
                data.getEmail(),
                data.getAddress(),
                data.getWorkingHours(),
                data.getFacebookUrl(),
                data.getInstagramUrl()
        );
        
        ShopInfoResponse response = ShopInfoResponse.from(shopInfo);
        return ResponseEntity.ok(BaseResponse.success(response, "Shop information updated successfully"));
    }
}
