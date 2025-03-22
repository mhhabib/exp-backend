package com.tomjerry.expensetracker.repository;

import com.tomjerry.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // Find expenses by user ID
    List<Expense> findByUserId(Long userId);

    // Find expenses by category ID
    List<Expense> findByCategoryId(Long categoryId);

    // Find expenses by user ID and category ID
    List<Expense> findByUserIdAndCategoryId(Long userId, Long categoryId);

    List<Expense> findByUserIdAndCreatedDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Expense> findByCreatedDateBetween(LocalDateTime startOfYear, LocalDateTime endOfYear);
}