package com.jett.flowershop.presentation.response;

/**
 * Response object for Category API.
 *
 * Fields:
 * - id
 * - name
 * - description
 */
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static CategoryResponse from(com.jett.flowershop.domain.entity.Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
