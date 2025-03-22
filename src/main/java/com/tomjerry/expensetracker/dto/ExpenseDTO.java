package com.tomjerry.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExpenseDTO {
    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Long userId;
    private Long categoryId;

    // Constructors
    public ExpenseDTO() {
    }

    public ExpenseDTO(Long id, String description, BigDecimal amount,
                      LocalDateTime createdDate, LocalDateTime updatedDate,
                      Long userId, Long categoryId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.userId = userId;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}