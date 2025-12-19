package com.jett.flowershop.presentation.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for updating a Category.
 *
 * Fields:
 * - name
 * - description
 *
 * Validation required.
 */
public class UpdateCategoryRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    public UpdateCategoryRequest() {
    }

    public UpdateCategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
