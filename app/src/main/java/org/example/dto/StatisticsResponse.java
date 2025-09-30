package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsResponse {
    private Double totalAmount;
    private Long totalExpenses;
    private Map<String, Double> expensesByCategory;
    private Map<String, Double> expensesByMonth;
    private Double averageExpense;
}