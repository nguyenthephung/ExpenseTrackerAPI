package org.example.service;

import org.example.dto.ExpenseRequest;
import org.example.dto.ExpenseResponse;
import org.example.dto.StatisticsResponse;
import org.example.model.Expense;
import org.example.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private AuthService authService;

    public ExpenseResponse createExpense(ExpenseRequest request) throws ExecutionException, InterruptedException {
        String currentUserId = authService.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDate(request.getDate() != null ? request.getDate() : LocalDateTime.now());
        expense.setUserId(currentUserId); // Set current user ID

        String id = expenseRepository.save(expense);
        return convertToResponse(expenseRepository.findById(id));
    }

    public ExpenseResponse updateExpense(String id, ExpenseRequest request) throws ExecutionException, InterruptedException {
        String currentUserId = authService.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        Expense expense = expenseRepository.findById(id);
        if (expense == null) {
            throw new RuntimeException("Expense not found with id: " + id);
        }

        // Check if expense belongs to current user
        if (!currentUserId.equals(expense.getUserId())) {
            throw new RuntimeException("Access denied: This expense does not belong to you");
        }

        expense.setTitle(request.getTitle());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        if (request.getDate() != null) {
            expense.setDate(request.getDate());
        }

        expenseRepository.save(expense);
        return convertToResponse(expense);
    }

    public ExpenseResponse getExpenseById(String id) throws ExecutionException, InterruptedException {
        String currentUserId = authService.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        Expense expense = expenseRepository.findById(id);
        if (expense == null) {
            throw new RuntimeException("Expense not found with id: " + id);
        }

        // Check if expense belongs to current user
        if (!currentUserId.equals(expense.getUserId())) {
            throw new RuntimeException("Access denied: This expense does not belong to you");
        }

        return convertToResponse(expense);
    }

    public List<ExpenseResponse> getAllExpenses() throws ExecutionException, InterruptedException {
        String currentUserId = authService.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        List<Expense> expenses = expenseRepository.findByUserId(currentUserId);
        return expenses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponse> getExpensesByCategory(String category) throws ExecutionException, InterruptedException {
        List<Expense> expenses = expenseRepository.findByCategory(category);
        return expenses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ExpenseResponse> getExpensesByDateRange(LocalDateTime startDate, LocalDateTime endDate) 
            throws ExecutionException, InterruptedException {
        List<Expense> expenses = expenseRepository.findByDateRange(startDate, endDate);
        return expenses.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void deleteExpense(String id) throws ExecutionException, InterruptedException {
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    public StatisticsResponse getStatistics() throws ExecutionException, InterruptedException {
        List<Expense> expenses = expenseRepository.findAll();
        
        if (expenses.isEmpty()) {
            return new StatisticsResponse(0.0, 0L, Map.of(), Map.of(), 0.0);
        }

        double totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum();
        long totalExpenses = expenses.size();
        double averageExpense = totalAmount / totalExpenses;

        Map<String, Double> expensesByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));

        Map<String, Double> expensesByMonth = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> YearMonth.from(expense.getDate()).format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        return new StatisticsResponse(totalAmount, totalExpenses, expensesByCategory, expensesByMonth, averageExpense);
    }

    public StatisticsResponse getStatisticsByDateRange(LocalDateTime startDate, LocalDateTime endDate) 
            throws ExecutionException, InterruptedException {
        List<Expense> expenses = expenseRepository.findByDateRange(startDate, endDate);
        
        if (expenses.isEmpty()) {
            return new StatisticsResponse(0.0, 0L, Map.of(), Map.of(), 0.0);
        }

        double totalAmount = expenses.stream().mapToDouble(Expense::getAmount).sum();
        long totalExpenses = expenses.size();
        double averageExpense = totalAmount / totalExpenses;

        Map<String, Double> expensesByCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));

        Map<String, Double> expensesByMonth = expenses.stream()
                .collect(Collectors.groupingBy(
                        expense -> YearMonth.from(expense.getDate()).format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        return new StatisticsResponse(totalAmount, totalExpenses, expensesByCategory, expensesByMonth, averageExpense);
    }

    private ExpenseResponse convertToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDate(),
                expense.getUserId(),
                expense.getCreatedAt(),
                expense.getUpdatedAt()
        );
    }
}