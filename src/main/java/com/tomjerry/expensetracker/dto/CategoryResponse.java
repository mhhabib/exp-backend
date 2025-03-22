package com.tomjerry.expensetracker.dto;

public class CategoryResponse {

    private Long id;
    private String name;

    // Constructor with parameters
    public CategoryResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // toString method
    @Override
    public String toString() {
        return "CategoryResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}