package com.jett.flowershop.infrastructure.repository;

import com.jett.flowershop.domain.entity.Review;
import com.jett.flowershop.domain.repository.ReviewRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {
    
    private final Map<Long, Review> storage = new ConcurrentHashMap<>();

    @Override
    public Review save(Review review) {
        storage.put(review.getId(), review);
        return review;
    }

    @Override
    public Optional<Review> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Review> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Review> findFeaturedReviews() {
        return storage.values().stream()
                .filter(Review::isFeatured)
                .filter(Review::isApproved)
                .filter(review -> "APPROVED".equals(review.getStatus()))
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findApprovedReviews() {
        return storage.values().stream()
                .filter(review -> "APPROVED".equals(review.getStatus()))
                .filter(Review::isApproved)
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByFlowerId(Long flowerId) {
        return storage.values().stream()
                .filter(review -> flowerId.equals(review.getFlowerId()))
                .filter(review -> "APPROVED".equals(review.getStatus()))
                .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}
