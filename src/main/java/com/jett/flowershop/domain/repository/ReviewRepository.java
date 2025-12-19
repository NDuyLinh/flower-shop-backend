package com.jett.flowershop.domain.repository;

import com.jett.flowershop.domain.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Review save(Review review);
    Optional<Review> findById(Long id);
    List<Review> findAll();
    List<Review> findFeaturedReviews();
    List<Review> findApprovedReviews();
    List<Review> findByFlowerId(Long flowerId);
    void deleteById(Long id);
    boolean existsById(Long id);
}
