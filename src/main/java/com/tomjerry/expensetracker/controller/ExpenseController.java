package com.tomjerry.expensetracker.controller;

import com.tomjerry.expensetracker.dto.ExpenseDTO;
import com.tomjerry.expensetracker.dto.ExpenseSummaryDTO;
import com.tomjerry.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    // Create a new expense
    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO createdExpense = expenseService.createExpense(expenseDTO);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    // Get all expenses
    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpenses() {
        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    // Get expenses for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByUser(@PathVariable Long userId) {
        List<ExpenseDTO> expenses = expenseService.getExpensesByUser(userId);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    // Update an existing expense
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseDTO expenseDTO) {
        ExpenseDTO updatedExpense = expenseService.updateExpense(id, expenseDTO);
        return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
    }

    // Delete an expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/current-month")
    public ResponseEntity<ExpenseSummaryDTO> getCurrentMonthExpenses() {
        ExpenseSummaryDTO monthExpenses = expenseService.getCurrentMonthExpenses();
        return new ResponseEntity<>(monthExpenses, HttpStatus.OK);
    }

    // Get current year's expenses summary
    @GetMapping("/current-year")
    public ResponseEntity<ExpenseSummaryDTO> getCurrentYearExpenses() {
        ExpenseSummaryDTO yearExpenses = expenseService.getCurrentYearExpenses();
        return new ResponseEntity<>(yearExpenses, HttpStatus.OK);
    }

    // Get expenses for a specific date range without user id
    @GetMapping("/date-between")
    public ResponseEntity<ExpenseSummaryDTO> getExpensesByDateRangeWithoutUserId(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        ExpenseSummaryDTO rangeExpenses = expenseService.getExpensesByDateRangeWithoutUserId(fromDate, toDate);
        return new ResponseEntity<>(rangeExpenses, HttpStatus.OK);
    }

    // Get expenses for a specific date range and user
    @GetMapping("/user/{userId}/date-between")
    public ResponseEntity<ExpenseSummaryDTO> getExpensesByDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        ExpenseSummaryDTO rangeExpenses = expenseService.getExpensesByDateRange(userId, fromDate, toDate);
        return new ResponseEntity<>(rangeExpenses, HttpStatus.OK);
    }
}