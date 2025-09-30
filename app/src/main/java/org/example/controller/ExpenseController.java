package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ExpenseRequest;
import org.example.dto.ExpenseResponse;
import org.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Expense Management", description = "APIs for managing expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Create a new expense", description = "Creates a new expense record")
    public ResponseEntity<ExpenseResponse> createExpense(@Valid @RequestBody ExpenseRequest request) 
            throws ExecutionException, InterruptedException {
        ExpenseResponse response = expenseService.createExpense(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an expense", description = "Updates an existing expense by ID")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @Parameter(description = "Expense ID") @PathVariable String id,
            @Valid @RequestBody ExpenseRequest request) throws ExecutionException, InterruptedException {
        ExpenseResponse response = expenseService.updateExpense(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get expense by ID", description = "Retrieves a specific expense by its ID")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @Parameter(description = "Expense ID") @PathVariable String id) 
            throws ExecutionException, InterruptedException {
        ExpenseResponse response = expenseService.getExpenseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all expenses", description = "Retrieves all expenses")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() 
            throws ExecutionException, InterruptedException {
        List<ExpenseResponse> responses = expenseService.getAllExpenses();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get expenses by category", description = "Retrieves expenses filtered by category")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(
            @Parameter(description = "Category name") @PathVariable String category) 
            throws ExecutionException, InterruptedException {
        List<ExpenseResponse> responses = expenseService.getExpensesByCategory(category);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get expenses by date range", description = "Retrieves expenses within a date range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) 
            throws ExecutionException, InterruptedException {
        List<ExpenseResponse> responses = expenseService.getExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an expense", description = "Deletes an expense by ID")
    public ResponseEntity<Void> deleteExpense(
            @Parameter(description = "Expense ID") @PathVariable String id) 
            throws ExecutionException, InterruptedException {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}