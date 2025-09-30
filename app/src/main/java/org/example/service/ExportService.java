package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.dto.ExpenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ExportService {

    @Autowired
    private ExpenseService expenseService;

    public String exportToJson() throws ExecutionException, InterruptedException {
        List<ExpenseResponse> expenses = expenseService.getAllExpenses();
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expenses);
        } catch (Exception e) {
            throw new RuntimeException("Failed to export to JSON", e);
        }
    }

    public String exportToJsonByDateRange(LocalDateTime startDate, LocalDateTime endDate) 
            throws ExecutionException, InterruptedException {
        List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expenses);
        } catch (Exception e) {
            throw new RuntimeException("Failed to export to JSON", e);
        }
    }

    public String exportToCsv() throws ExecutionException, InterruptedException {
        List<ExpenseResponse> expenses = expenseService.getAllExpenses();
        return convertToCsv(expenses);
    }

    public String exportToCsvByDateRange(LocalDateTime startDate, LocalDateTime endDate) 
            throws ExecutionException, InterruptedException {
        List<ExpenseResponse> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
        return convertToCsv(expenses);
    }

    private String convertToCsv(List<ExpenseResponse> expenses) {
        StringWriter stringWriter = new StringWriter();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        // CSV Header
        stringWriter.append("ID,Title,Description,Amount,Category,Date,User ID,Created At,Updated At\n");
        
        // CSV Data
        for (ExpenseResponse expense : expenses) {
            stringWriter.append(escapeCsvValue(expense.getId())).append(",");
            stringWriter.append(escapeCsvValue(expense.getTitle())).append(",");
            stringWriter.append(escapeCsvValue(expense.getDescription())).append(",");
            stringWriter.append(String.valueOf(expense.getAmount())).append(",");
            stringWriter.append(escapeCsvValue(expense.getCategory())).append(",");
            stringWriter.append(expense.getDate() != null ? expense.getDate().format(formatter) : "").append(",");
            stringWriter.append(escapeCsvValue(expense.getUserId())).append(",");
            stringWriter.append(expense.getCreatedAt() != null ? expense.getCreatedAt().format(formatter) : "").append(",");
            stringWriter.append(expense.getUpdatedAt() != null ? expense.getUpdatedAt().format(formatter) : "");
            stringWriter.append("\n");
        }
        
        return stringWriter.toString();
    }

    private String escapeCsvValue(String value) {
        if (value == null) {
            return "";
        }
        
        // Escape quotes and wrap in quotes if necessary
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        
        return value;
    }
}