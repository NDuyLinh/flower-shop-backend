package com.jett.flowershop.presentation.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for creating a Category.
 *
 * Fields:
 * - name
 * - description
 *
 * Validation required.
 */
public class CreateCategoryRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    public CreateCategoryRequest() {
    }

    public CreateCategoryRequest(String name, String description) {
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
