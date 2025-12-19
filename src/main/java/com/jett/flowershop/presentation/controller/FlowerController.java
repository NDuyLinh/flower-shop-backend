package com.jett.flowershop.presentation.controller;

import com.jett.flowershop.application.dto.ImageUploadResult;
import com.jett.flowershop.application.service.FlowerService;
import com.jett.flowershop.application.service.ImageService;
import com.jett.flowershop.domain.entity.Flower;
import com.jett.flowershop.presentation.request.BaseRequest;
import com.jett.flowershop.presentation.request.CreateFlowerRequest;
import com.jett.flowershop.presentation.request.UpdateFlowerRequest;
import com.jett.flowershop.presentation.response.BaseResponse;
import com.jett.flowershop.presentation.response.FlowerResponse;
import com.jett.flowershop.presentation.response.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for Flower API.
 *
 * Endpoints:
 * - POST /api/flowers - Create flower
 * - GET /api/flowers - Get all flowers
 * - GET /api/flowers/{id} - Get flower by ID
 * - PUT /api/flowers/{id} - Update flower
 * - DELETE /api/flowers/{id} - Delete flower
 *
 * Request/Response:
 * - Uses BaseRequest/BaseResponse wrapper
 *
 * Rules:
 * - Extract requestParameter
 * - No business logic
 */
@Tag(name = "Flower", description = "Flower management APIs")
@RestController
@RequestMapping("/api/flowers")
public class FlowerController {

    private final FlowerService flowerService;
    private final ImageService imageService;

    public FlowerController(FlowerService flowerService, ImageService imageService) {
        this.flowerService = flowerService;
        this.imageService = imageService;
    }

    @Operation(
        summary = "Create a new flower with image upload",
        description = "Creates a new flower with image processing and multiple sizes generation"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<FlowerResponse>> createFlower(
            @RequestParam("file") MultipartFile file,
            @RequestParam("name") String name,
            @RequestParam("price") java.math.BigDecimal price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "occasion", required = false) String occasion,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam("productSlug") String productSlug,
            @RequestParam(value = "stockQuantity", required = false, defaultValue = "0") Integer stockQuantity,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "isFeatured", required = false, defaultValue = "false") Boolean isFeatured,
            @RequestParam(value = "isHot", required = false, defaultValue = "false") Boolean isHot) {
        
        try {
            // Process and upload image
            ImageUploadResult imageResult = imageService.uploadProductImage(file, productSlug);

            // Create flower entity
            Flower flower = new Flower();
            flower.setName(name);
            flower.setPrice(price);
            flower.setDescription(description);
            flower.setOccasion(occasion);
            flower.setColor(color);
            flower.setImageUrl(imageResult.getLargeUrl()); // Use large image as main URL
            flower.setStockQuantity(stockQuantity);
            flower.setCategoryId(categoryId);
            flower.setIsFeatured(isFeatured);
            flower.setIsHot(isHot);
            flower.setViewCount(0);
            flower.setSoldCount(0);
            flower.setStatus("ACTIVE");
            flower.setCreatedAt(LocalDateTime.now());
            flower.setUpdatedAt(LocalDateTime.now());

            // Save to database
            Flower createdFlower = flowerService.createFlower(flower);

            // Map to response
            FlowerResponse flowerResponse = new FlowerResponse(
                    createdFlower.getId(),
                    createdFlower.getName(),
                    createdFlower.getPrice(),
                    createdFlower.getDescription(),
                    createdFlower.getOccasion(),
                    createdFlower.getColor()
            );

            // Build response
            ResponseStatus status = new ResponseStatus("00", "Flower created successfully with images");
            BaseResponse<FlowerResponse> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    flowerResponse
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            ResponseStatus status = new ResponseStatus("02", "Validation error: " + e.getMessage());
            BaseResponse<FlowerResponse> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        } catch (Exception e) {
            ResponseStatus status = new ResponseStatus("01", "Error processing image: " + e.getMessage());
            BaseResponse<FlowerResponse> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Create a new flower (JSON)", description = "Creates a new flower in the shop using JSON request")
    @PostMapping(path = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<FlowerResponse>> createFlowerJson(
            @Valid @RequestBody BaseRequest<CreateFlowerRequest> request) {
        
        // Extract requestParameter
        CreateFlowerRequest createRequest = request.getRequestParameter();
        
        // Map to domain entity
        Flower flower = new Flower();
        flower.setName(createRequest.getName());
        flower.setPrice(createRequest.getPrice());
        flower.setDescription(createRequest.getDescription());
        flower.setOccasion(createRequest.getOccasion());
        flower.setColor(createRequest.getColor());

        // Call service
        Flower createdFlower = flowerService.createFlower(flower);

        // Map to response
        FlowerResponse flowerResponse = new FlowerResponse(
                createdFlower.getId(),
                createdFlower.getName(),
                createdFlower.getPrice(),
                createdFlower.getDescription(),
                createdFlower.getOccasion(),
                createdFlower.getColor()
        );

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<FlowerResponse> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                flowerResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all flowers", description = "Retrieves a list of all flowers in the shop")
    @GetMapping
    public ResponseEntity<BaseResponse<List<FlowerResponse>>> getAllFlowers() {
        
        // Call service
        List<Flower> flowers = flowerService.getAllFlowers();

        // Map to response list
        List<FlowerResponse> flowerResponses = flowers.stream()
                .map(flower -> new FlowerResponse(
                        flower.getId(),
                        flower.getName(),
                        flower.getPrice(),
                        flower.getDescription(),
                        flower.getOccasion(),
                        flower.getColor()
                ))
                .collect(Collectors.toList());

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<List<FlowerResponse>> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                flowerResponses
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get flower by ID", description = "Retrieves a specific flower by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<FlowerResponse>> getFlowerById(@PathVariable Long id) {
        
        // Call service
        Optional<Flower> flowerOpt = flowerService.getFlowerById(id);

        if (flowerOpt.isEmpty()) {
            ResponseStatus status = new ResponseStatus("03", "Flower not found with id: " + id);
            BaseResponse<FlowerResponse> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Map to response
        Flower flower = flowerOpt.get();
        FlowerResponse flowerResponse = new FlowerResponse(
                flower.getId(),
                flower.getName(),
                flower.getPrice(),
                flower.getDescription(),
                flower.getOccasion(),
                flower.getColor()
        );

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<FlowerResponse> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                flowerResponse
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update flower", description = "Updates an existing flower")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<FlowerResponse>> updateFlower(
            @PathVariable Long id,
            @Valid @RequestBody BaseRequest<UpdateFlowerRequest> request) {
        
        try {
            // Extract requestParameter
            UpdateFlowerRequest updateRequest = request.getRequestParameter();
            
            // Map to domain entity
            Flower flower = new Flower();
            flower.setName(updateRequest.getName());
            flower.setPrice(updateRequest.getPrice());
            flower.setDescription(updateRequest.getDescription());
            flower.setOccasion(updateRequest.getOccasion());
            flower.setColor(updateRequest.getColor());

            // Call service
            Flower updatedFlower = flowerService.updateFlower(id, flower);

            // Map to response
            FlowerResponse flowerResponse = new FlowerResponse(
                    updatedFlower.getId(),
                    updatedFlower.getName(),
                    updatedFlower.getPrice(),
                    updatedFlower.getDescription(),
                    updatedFlower.getOccasion(),
                    updatedFlower.getColor()
            );

            // Build base response
            ResponseStatus status = new ResponseStatus("00", "Success");
            BaseResponse<FlowerResponse> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    flowerResponse
            );

            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            ResponseStatus status = new ResponseStatus("03", e.getMessage());
            BaseResponse<FlowerResponse> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Delete flower", description = "Deletes a flower by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteFlower(@PathVariable Long id) {
        
        try {
            // Call service
            flowerService.deleteFlower(id);

            // Build base response
            ResponseStatus status = new ResponseStatus("00", "Flower deleted successfully");
            BaseResponse<Void> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    null
            );

            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            ResponseStatus status = new ResponseStatus("03", e.getMessage());
            BaseResponse<Void> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Search flowers by name", description = "Search flowers by name keyword")
    @GetMapping("/search/name")
    public ResponseEntity<BaseResponse<List<FlowerResponse>>> searchFlowersByName(
            @RequestParam String name) {
        
        // Call service
        List<Flower> flowers = flowerService.searchFlowersByName(name);

        // Map to response list
        List<FlowerResponse> flowerResponses = flowers.stream()
                .map(flower -> new FlowerResponse(
                        flower.getId(),
                        flower.getName(),
                        flower.getPrice(),
                        flower.getDescription(),
                        flower.getOccasion(),
                        flower.getColor()
                ))
                .collect(Collectors.toList());

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<List<FlowerResponse>> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                flowerResponses
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter flowers by occasion", description = "Filter flowers by occasion")
    @GetMapping("/filter/occasion")
    public ResponseEntity<BaseResponse<List<FlowerResponse>>> filterFlowersByOccasion(
            @RequestParam String occasion) {
        
        // Call service
        List<Flower> flowers = flowerService.filterFlowersByOccasion(occasion);

        // Map to response list
        List<FlowerResponse> flowerResponses = flowers.stream()
                .map(flower -> new FlowerResponse(
                        flower.getId(),
                        flower.getName(),
                        flower.getPrice(),
                        flower.getDescription(),
                        flower.getOccasion(),
                        flower.getColor()
                ))
                .collect(Collectors.toList());

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<List<FlowerResponse>> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                flowerResponses
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter flowers by color", description = "Filter flowers by color")
    @GetMapping("/filter/color")
    public ResponseEntity<BaseResponse<List<FlowerResponse>>> filterFlowersByColor(
            @RequestParam String color) {
        
        // Call service
        List<Flower> flowers = flowerService.filterFlowersByColor(color);

        // Map to response list
        List<FlowerResponse> flowerResponses = flowers.stream()
                .map(flower -> new FlowerResponse(
                        flower.getId(),
                        flower.getName(),
                        flower.getPrice(),
                        flower.getDescription(),
                        flower.getOccasion(),
                        flower.getColor()
                ))
                .collect(Collectors.toList());

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<List<FlowerResponse>> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                flowerResponses
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter flowers by price range", description = "Filter flowers by minimum and maximum price")
    @GetMapping("/filter/price")
    public ResponseEntity<BaseResponse<List<FlowerResponse>>> filterFlowersByPriceRange(
            @RequestParam(required = false) java.math.BigDecimal minPrice,
            @RequestParam(required = false) java.math.BigDecimal maxPrice) {
        
        // Call service
        List<Flower> flowers = flowerService.filterFlowersByPriceRange(minPrice, maxPrice);

        // Map to response list
        List<FlowerResponse> flowerResponses = flowers.stream()
                .map(flower -> new FlowerResponse(
                        flower.getId(),
                        flower.getName(),
                        flower.getPrice(),
                        flower.getDescription(),
                        flower.getOccasion(),
                        flower.getColor()
                ))
                .collect(Collectors.toList());

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<List<FlowerResponse>> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                flowerResponses
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search flowers by keyword", description = "Search flowers by name or occasion keyword")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<FlowerResponse>>> searchFlowers(
            @RequestParam String keyword) {
        
        // Call service
        List<Flower> flowers = flowerService.searchFlowersByNameOrOccasion(keyword);

        // Map to response list
        List<FlowerResponse> flowerResponses = flowers.stream()
                .map(flower -> new FlowerResponse(
                        flower.getId(),
                        flower.getName(),
                        flower.getPrice(),
                        flower.getDescription(),
                        flower.getOccasion(),
                        flower.getColor()
                ))
                .collect(Collectors.toList());

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<List<FlowerResponse>> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                flowerResponses
        );

        return ResponseEntity.ok(response);
    }
}
