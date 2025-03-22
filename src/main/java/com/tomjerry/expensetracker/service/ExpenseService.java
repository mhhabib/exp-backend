package com.tomjerry.expensetracker.service;

import com.tomjerry.expensetracker.dto.ExpenseDTO;
import com.tomjerry.expensetracker.dto.ExpenseSummaryDTO;
import com.tomjerry.expensetracker.model.Category;
import com.tomjerry.expensetracker.model.Expense;
import com.tomjerry.expensetracker.model.User;
import com.tomjerry.expensetracker.repository.CategoryRepository;
import com.tomjerry.expensetracker.repository.ExpenseRepository;
import com.tomjerry.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Create new expense
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        // Fetch user and category
        User user = userRepository.findById(expenseDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        LocalDateTime createdDate = expenseDTO.getCreatedDate();
        ZonedDateTime createdDateInUTC = createdDate.atZone(ZoneOffset.UTC);

        Expense expense = new Expense();
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setUser(user);
        expense.setCategory(category);
        expense.setCreatedDate(createdDateInUTC.toLocalDateTime());
        expense.setUpdatedDate(createdDateInUTC.toLocalDateTime());

        // Save and return DTO
        Expense savedExpense = expenseRepository.save(expense);
        return mapToDTO(savedExpense);
    }

    // Read all expenses
    public List<ExpenseDTO> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Read expenses by user
    public List<ExpenseDTO> getExpensesByUser(Long userId) {
        return expenseRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Update expense
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        // Update expense details
        existingExpense.setDescription(expenseDTO.getDescription());
        existingExpense.setAmount(expenseDTO.getAmount());
        existingExpense.setUpdatedDate(LocalDateTime.now());

        // Update category if provided
        if (expenseDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingExpense.setCategory(category);
        }

        Expense updatedExpense = expenseRepository.save(existingExpense);
        return mapToDTO(updatedExpense);
    }

    // Delete expense
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expenseRepository.delete(expense);
    }

    // Utility method to map Expense to ExpenseDTO
    private ExpenseDTO mapToDTO(Expense expense) {
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(expense.getId());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        dto.setCreatedDate(expense.getCreatedDate());
        dto.setUpdatedDate(expense.getUpdatedDate());
        dto.setUserId(expense.getUser().getId());
        dto.setCategoryId(expense.getCategory().getId());
        return dto;
    }

    public ExpenseSummaryDTO getCurrentMonthExpenses() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime endOfMonth = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).truncatedTo(ChronoUnit.SECONDS);

        List<Expense> monthExpenses = expenseRepository.findByCreatedDateBetween(startOfMonth, endOfMonth);

        // Calculate total expenses
        BigDecimal totalExpenses = monthExpenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate categorical totals
        Map<String, BigDecimal> categoricalTotals = monthExpenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getName(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));

        // Map expenses to DTOs
        List<ExpenseDTO> expenseList = monthExpenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ExpenseSummaryDTO(totalExpenses, categoricalTotals, expenseList);
    }

    // Get current year's expenses summary
    public ExpenseSummaryDTO getCurrentYearExpenses() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime startOfYear = now.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0).truncatedTo(ChronoUnit.SECONDS);;
        LocalDateTime endOfYear = now.withDayOfYear(now.toLocalDate().lengthOfYear())
                .withHour(23).withMinute(59).withSecond(59).truncatedTo(ChronoUnit.SECONDS);;

        List<Expense> yearExpenses = expenseRepository.findByCreatedDateBetween(startOfYear, endOfYear);

        // Calculate total expenses
        BigDecimal totalExpenses = yearExpenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate categorical totals
        Map<String, BigDecimal> categoricalTotals = yearExpenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getName(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));

        // Map expenses to DTOs
        List<ExpenseDTO> expenseList = yearExpenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ExpenseSummaryDTO(totalExpenses, categoricalTotals, expenseList);
    }

    // Get expenses for a specific date range and user
    public ExpenseSummaryDTO getExpensesByDateRange(Long userId, LocalDate fromDate, LocalDate toDate) {

        if (fromDate == null && toDate == null) {
            YearMonth currentMonth = YearMonth.now();
            fromDate = currentMonth.atDay(1);
            toDate = currentMonth.atEndOfMonth();
        } else if (fromDate == null) {
            fromDate = YearMonth.from(toDate).atDay(1);
        } else if (toDate == null) {
            toDate = YearMonth.from(fromDate).atEndOfMonth();
        }

        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.atTime(23, 59, 59);

        List<Expense> rangeExpenses = expenseRepository.findByUserIdAndCreatedDateBetween(
                userId, startDateTime, endDateTime
        );

        BigDecimal totalExpenses = rangeExpenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> categoricalTotals = rangeExpenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

        List<ExpenseDTO> expenseList = rangeExpenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ExpenseSummaryDTO(totalExpenses, categoricalTotals, expenseList);
    }

    public ExpenseSummaryDTO getExpensesByDateRangeWithoutUserId(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null && toDate == null) {
            YearMonth currentMonth = YearMonth.now();
            fromDate = currentMonth.atDay(1);
            toDate = currentMonth.atEndOfMonth();
        } else if (fromDate == null) {
            fromDate = YearMonth.from(toDate).atDay(1);
        } else if (toDate == null) {
            toDate = YearMonth.from(fromDate).atEndOfMonth();
        }

        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.atTime(23, 59, 59);

        List<Expense> rangeExpenses = expenseRepository.findByCreatedDateBetween(startDateTime, endDateTime);

        BigDecimal totalExpenses = rangeExpenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> categoricalTotals = rangeExpenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));

        List<ExpenseDTO> expenseList = rangeExpenses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new ExpenseSummaryDTO(totalExpenses, categoricalTotals, expenseList);
    }
}