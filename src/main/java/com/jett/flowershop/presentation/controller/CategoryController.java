package com.jett.flowershop.presentation.controller;

import com.jett.flowershop.application.service.CategoryService;
import com.jett.flowershop.domain.entity.Category;
import com.jett.flowershop.presentation.request.BaseRequest;
import com.jett.flowershop.presentation.request.CreateCategoryRequest;
import com.jett.flowershop.presentation.request.UpdateCategoryRequest;
import com.jett.flowershop.presentation.response.BaseResponse;
import com.jett.flowershop.presentation.response.CategoryResponse;
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
 * REST controller for Category API.
 *
 * Endpoints:
 * - POST /api/categories - Create category
 * - GET /api/categories - Get all categories
 * - GET /api/categories/{id} - Get category by ID
 * - PUT /api/categories/{id} - Update category
 * - DELETE /api/categories/{id} - Delete category
 *
 * Request/Response:
 * - Uses BaseRequest/BaseResponse wrapper
 *
 * Rules:
 * - Extract requestParameter
 * - No business logic
 */
@Tag(name = "Category", description = "Category management APIs")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Create a new category", description = "Creates a new category in the shop")
    @PostMapping
    public ResponseEntity<BaseResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody BaseRequest<CreateCategoryRequest> request) {
        
        // Extract requestParameter
        CreateCategoryRequest createRequest = request.getRequestParameter();
        
        // Map to domain entity
        Category category = new Category();
        category.setName(createRequest.getName());
        category.setDescription(createRequest.getDescription());

        // Call service
        Category createdCategory = categoryService.createCategory(category);

        // Map to response
        CategoryResponse categoryResponse = new CategoryResponse(
                createdCategory.getId(),
                createdCategory.getName(),
                createdCategory.getDescription()
        );

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<CategoryResponse> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                categoryResponse
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories in the shop")
    @GetMapping
    public ResponseEntity<BaseResponse<List<CategoryResponse>>> getAllCategories() {
        
        // Call service
        List<Category> categories = categoryService.getAllCategories();

        // Map to response list
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()
                ))
                .collect(Collectors.toList());

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<List<CategoryResponse>> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                categoryResponses
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        
        // Call service
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);

        if (categoryOpt.isEmpty()) {
            ResponseStatus status = new ResponseStatus("03", "Category not found with id: " + id);
            BaseResponse<CategoryResponse> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Map to response
        Category category = categoryOpt.get();
        CategoryResponse categoryResponse = new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );

        // Build base response
        ResponseStatus status = new ResponseStatus("00", "Success");
        BaseResponse<CategoryResponse> response = new BaseResponse<>(
                LocalDateTime.now(),
                status,
                categoryResponse
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update category", description = "Updates an existing category")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody BaseRequest<UpdateCategoryRequest> request) {
        
        try {
            // Extract requestParameter
            UpdateCategoryRequest updateRequest = request.getRequestParameter();
            
            // Map to domain entity
            Category category = new Category();
            category.setName(updateRequest.getName());
            category.setDescription(updateRequest.getDescription());

            // Call service
            Category updatedCategory = categoryService.updateCategory(id, category);

            // Map to response
            CategoryResponse categoryResponse = new CategoryResponse(
                    updatedCategory.getId(),
                    updatedCategory.getName(),
                    updatedCategory.getDescription()
            );

            // Build base response
            ResponseStatus status = new ResponseStatus("00", "Success");
            BaseResponse<CategoryResponse> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    categoryResponse
            );

            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            ResponseStatus status = new ResponseStatus("03", e.getMessage());
            BaseResponse<CategoryResponse> response = new BaseResponse<>(
                    LocalDateTime.now(),
                    status,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Delete category", description = "Deletes a category by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteCategory(@PathVariable Long id) {
        
        try {
            // Call service
            categoryService.deleteCategory(id);

            // Build base response
            ResponseStatus status = new ResponseStatus("00", "Category deleted successfully");
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
}
