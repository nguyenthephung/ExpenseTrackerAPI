package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/export")
@Tag(name = "Data Export", description = "APIs for exporting expense data")
@CrossOrigin(origins = "*")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/json")
    @Operation(summary = "Export expenses to JSON", description = "Exports all expenses in JSON format")
    public ResponseEntity<String> exportToJson() throws ExecutionException, InterruptedException {
        String jsonData = exportService.exportToJson();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expenses.json");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(jsonData);
    }

    @GetMapping("/json/date-range")
    @Operation(summary = "Export expenses to JSON by date range", description = "Exports expenses within date range in JSON format")
    public ResponseEntity<String> exportToJsonByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) 
            throws ExecutionException, InterruptedException {
        String jsonData = exportService.exportToJsonByDateRange(startDate, endDate);
        
        String filename = String.format("expenses_%s_to_%s.json", 
                startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(jsonData);
    }

    @GetMapping("/csv")
    @Operation(summary = "Export expenses to CSV", description = "Exports all expenses in CSV format")
    public ResponseEntity<String> exportToCsv() throws ExecutionException, InterruptedException {
        String csvData = exportService.exportToCsv();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expenses.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

    @GetMapping("/csv/date-range")
    @Operation(summary = "Export expenses to CSV by date range", description = "Exports expenses within date range in CSV format")
    public ResponseEntity<String> exportToCsvByDateRange(
            @Parameter(description = "Start date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date (yyyy-MM-dd'T'HH:mm:ss)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) 
            throws ExecutionException, InterruptedException {
        String csvData = exportService.exportToCsvByDateRange(startDate, endDate);
        
        String filename = String.format("expenses_%s_to_%s.csv", 
                startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }
}