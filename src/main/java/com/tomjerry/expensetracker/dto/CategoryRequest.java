package com.tomjerry.expensetracker.dto;


import jakarta.validation.constraints.NotBlank;

public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    // Default constructor
    public CategoryRequest() {
    }

    // Constructor with parameters
    public CategoryRequest(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // toString method
    @Override
    public String toString() {
        return "CategoryRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}