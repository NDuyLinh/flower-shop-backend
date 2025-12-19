package com.jett.flowershop.application.service;

import com.jett.flowershop.domain.entity.Category;
import com.jett.flowershop.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Application service handling Category use cases.
 *
 * Use cases:
 * - Create new Category
 * - Get all Categories
 * - Get Category by ID
 * - Update Category
 * - Delete Category
 *
 * Rules:
 * - Depends on domain only
 * - No Spring annotations
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(Category category) {
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category updateCategory(Long id, Category category) {
        Optional<Category> existing = categoryRepository.findById(id);
        if (existing.isEmpty()) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        
        Category existingCategory = existing.get();
        existingCategory.setName(category.getName());
        existingCategory.setDescription(category.getDescription());
        
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    // Homepage methods
    public List<Category> getFeaturedCategories() {
        return categoryRepository.findFeaturedCategories();
    }
}
