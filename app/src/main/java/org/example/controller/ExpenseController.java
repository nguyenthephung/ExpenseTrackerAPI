package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@Tag(name = "💰 Expense Management", description = "CRUD operations for expense records")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Operation(
        summary = "💳 Create New Expense",
        description = "Add a new expense record to your account",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Expense created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExpenseResponse.class),
                examples = @ExampleObject(
                    name = "Created Expense",
                    summary = "Successfully created expense",
                    value = """
                    {
                        "id": "expense-123",
                        "description": "Lunch at restaurant",
                        "amount": 25.50,
                        "categoryId": "food-category",
                        "categoryName": "Food & Dining",
                        "date": "2024-01-20T12:30:00",
                        "userId": "user-123",
                        "createdAt": "2024-01-20T12:30:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Expense details to create",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExpenseRequest.class),
                examples = @ExampleObject(
                    name = "Sample Expense",
                    summary = "Create a restaurant expense",
                    value = """
                    {
                        "description": "Lunch at restaurant",
                        "amount": 25.50,
                        "categoryId": "food-category",
                        "date": "2024-01-20T12:30:00"
                    }
                    """
                )
            )
        )
        @Valid @RequestBody ExpenseRequest request) 
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

    @Operation(
        summary = "📋 Get All Expenses",
        description = "Retrieve all expense records for the current user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Expenses retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Expense List",
                    summary = "List of user expenses",
                    value = """
                    [
                        {
                            "id": "expense-123",
                            "description": "Lunch at restaurant",
                            "amount": 25.50,
                            "categoryId": "food-category",
                            "categoryName": "Food & Dining",
                            "date": "2024-01-20T12:30:00",
                            "userId": "user-123",
                            "createdAt": "2024-01-20T12:30:00"
                        },
                        {
                            "id": "expense-124",
                            "description": "Gas station",
                            "amount": 45.00,
                            "categoryId": "transport-category",
                            "categoryName": "Transportation",
                            "date": "2024-01-19T08:15:00",
                            "userId": "user-123",
                            "createdAt": "2024-01-19T08:15:00"
                        }
                    ]
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @GetMapping
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