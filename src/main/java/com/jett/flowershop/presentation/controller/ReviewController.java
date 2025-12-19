package com.jett.flowershop.presentation.controller;

import com.jett.flowershop.application.service.ReviewService;
import com.jett.flowershop.domain.entity.Review;
import com.jett.flowershop.presentation.request.CreateReviewRequest;
import com.jett.flowershop.presentation.request.BaseRequest;
import com.jett.flowershop.presentation.response.BaseResponse;
import com.jett.flowershop.presentation.response.ReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review", description = "Review management APIs")
public class ReviewController {
    
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    @Operation(summary = "Create a new review", description = "Submit a new customer review")
    public ResponseEntity<BaseResponse<ReviewResponse>> createReview(
            @Valid @RequestBody BaseRequest<CreateReviewRequest> request) {
        CreateReviewRequest data = request.getData();
        Review review = reviewService.createReview(
                data.getCustomerName(),
                data.getCustomerEmail(),
                data.getCustomerAvatar(),
                data.getRating(),
                data.getComment(),
                data.getFlowerId()
        );
        
        ReviewResponse response = ReviewResponse.from(review);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(response, "Review created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all reviews", description = "Retrieve all reviews (for admin)")
    public ResponseEntity<BaseResponse<List<ReviewResponse>>> getAllReviews() {
        List<ReviewResponse> reviews = reviewService.getAllReviews().stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success(reviews, "Reviews retrieved successfully"));
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured reviews", description = "Retrieve featured and approved reviews for homepage")
    public ResponseEntity<BaseResponse<List<ReviewResponse>>> getFeaturedReviews() {
        List<ReviewResponse> reviews = reviewService.getFeaturedReviews().stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success(reviews, "Featured reviews retrieved successfully"));
    }

    @GetMapping("/approved")
    @Operation(summary = "Get approved reviews", description = "Retrieve all approved reviews")
    public ResponseEntity<BaseResponse<List<ReviewResponse>>> getApprovedReviews() {
        List<ReviewResponse> reviews = reviewService.getApprovedReviews().stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success(reviews, "Approved reviews retrieved successfully"));
    }

    @GetMapping("/flower/{flowerId}")
    @Operation(summary = "Get reviews by flower", description = "Retrieve approved reviews for a specific flower")
    public ResponseEntity<BaseResponse<List<ReviewResponse>>> getReviewsByFlower(
            @PathVariable Long flowerId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByFlowerId(flowerId).stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponse.success(reviews, "Reviews retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID", description = "Retrieve a review by its ID")
    public ResponseEntity<BaseResponse<ReviewResponse>> getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        ReviewResponse response = ReviewResponse.from(review);
        return ResponseEntity.ok(BaseResponse.success(response, "Review retrieved successfully"));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve a review", description = "Approve a pending review")
    public ResponseEntity<BaseResponse<ReviewResponse>> approveReview(@PathVariable Long id) {
        Review review = reviewService.approveReview(id);
        ReviewResponse response = ReviewResponse.from(review);
        return ResponseEntity.ok(BaseResponse.success(response, "Review approved successfully"));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject a review", description = "Reject a pending review")
    public ResponseEntity<BaseResponse<ReviewResponse>> rejectReview(@PathVariable Long id) {
        Review review = reviewService.rejectReview(id);
        ReviewResponse response = ReviewResponse.from(review);
        return ResponseEntity.ok(BaseResponse.success(response, "Review rejected successfully"));
    }

    @PutMapping("/{id}/featured")
    @Operation(summary = "Set review as featured", description = "Mark a review as featured for homepage")
    public ResponseEntity<BaseResponse<ReviewResponse>> setFeatured(
            @PathVariable Long id,
            @RequestParam boolean featured) {
        Review review = reviewService.setFeatured(id, featured);
        ReviewResponse response = ReviewResponse.from(review);
        return ResponseEntity.ok(BaseResponse.success(response, "Review featured status updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a review", description = "Delete a review by its ID")
    public ResponseEntity<BaseResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(BaseResponse.success(null, "Review deleted successfully"));
    }
}
