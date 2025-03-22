package com.tomjerry.expensetracker.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ExpenseSummaryDTO {
    private BigDecimal totalExpenses;
    private Map<String, BigDecimal> categoricalTotals;
    private List<ExpenseDTO> expenses;

    // Constructors
    public ExpenseSummaryDTO() {
    }

    public ExpenseSummaryDTO(BigDecimal totalExpenses,
                             Map<String, BigDecimal> categoricalTotals,
                             List<ExpenseDTO> expenses) {
        this.totalExpenses = totalExpenses;
        this.categoricalTotals = categoricalTotals;
        this.expenses = expenses;
    }

    // Getters and Setters
    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Map<String, BigDecimal> getCategoricalTotals() {
        return categoricalTotals;
    }

    public void setCategoricalTotals(Map<String, BigDecimal> categoricalTotals) {
        this.categoricalTotals = categoricalTotals;
    }

    public List<ExpenseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDTO> expenses) {
        this.expenses = expenses;
    }
}