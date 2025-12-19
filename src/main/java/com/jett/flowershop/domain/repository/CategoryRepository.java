package com.jett.flowershop.domain.repository;

import com.jett.flowershop.domain.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * Domain repository interface for Category entity.
 *
 * Rules:
 * - No framework dependency
 * - Abstract persistence operations
 */
public interface CategoryRepository {

    Category save(Category category);

    Optional<Category> findById(Long id);

    List<Category> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // Homepage methods
    List<Category> findFeaturedCategories();
}
