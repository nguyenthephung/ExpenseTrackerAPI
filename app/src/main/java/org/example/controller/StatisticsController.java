package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.StatisticsResponse;
import org.example.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/statistics")
@Tag(name = "Statistics", description = "APIs for expense statistics and analytics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    @Operation(summary = "Get overall statistics", description = "Retrieves overall expense statistics")
    public ResponseEntity<StatisticsResponse> getStatistics() 
            throws ExecutionException, InterruptedException {
        StatisticsResponse response = expenseService.getStatistics();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get statistics by date range", description = "Retrieves expense statistics within a date range")
    public ResponseEntity<StatisticsResponse> getStatisticsByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) 
            throws ExecutionException, InterruptedException {
        StatisticsResponse response = expenseService.getStatisticsByDateRange(startDate, endDate);
        return ResponseEntity.ok(response);
    }
}