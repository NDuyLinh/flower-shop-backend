package com.jett.flowershop.application.service;

import com.jett.flowershop.domain.entity.Review;
import com.jett.flowershop.domain.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(String customerName, String customerEmail, String customerAvatar, 
                              Integer rating, String comment, Long flowerId) {
        Review review = new Review();
        review.setId(System.currentTimeMillis()); // Simple ID generation
        review.setCustomerName(customerName);
        review.setCustomerEmail(customerEmail);
        review.setCustomerAvatar(customerAvatar);
        review.setRating(rating);
        review.setComment(comment);
        review.setFlowerId(flowerId);
        review.setFeatured(false);
        review.setApproved(false);
        review.setStatus("PENDING");
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getFeaturedReviews() {
        return reviewRepository.findFeaturedReviews();
    }

    public List<Review> getApprovedReviews() {
        return reviewRepository.findApprovedReviews();
    }

    public List<Review> getReviewsByFlowerId(Long flowerId) {
        return reviewRepository.findByFlowerId(flowerId);
    }

    public Review approveReview(Long id) {
        Review review = getReviewById(id);
        review.setApproved(true);
        review.setStatus("APPROVED");
        return reviewRepository.save(review);
    }

    public Review rejectReview(Long id) {
        Review review = getReviewById(id);
        review.setApproved(false);
        review.setStatus("REJECTED");
        return reviewRepository.save(review);
    }

    public Review setFeatured(Long id, boolean featured) {
        Review review = getReviewById(id);
        review.setFeatured(featured);
        return reviewRepository.save(review);
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }
}
