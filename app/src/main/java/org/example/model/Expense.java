package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    private String id;
    private String title;
    private String description;
    private Double amount;
    private String category;
    private LocalDateTime date;
    private String userId; // For future user management
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}