package com.jett.flowershop.domain.repository;

import com.jett.flowershop.domain.entity.Flower;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for Flower entity.
 *
 * Rules:
 * - No framework dependency
 * - Abstract persistence operations
 */
public interface FlowerRepository {

    Flower save(Flower flower);

    Optional<Flower> findById(Long id);

    List<Flower> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // Search and filter methods
    List<Flower> searchByName(String name);

    List<Flower> findByOccasion(String occasion);

    List<Flower> findByColor(String color);

    List<Flower> findByPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice);

    List<Flower> searchByNameOrOccasion(String keyword);

    // Homepage methods
    List<Flower> findHotFlowers();

    List<Flower> findFeaturedFlowers();

    List<Flower> findBestSelling(int limit);
}
