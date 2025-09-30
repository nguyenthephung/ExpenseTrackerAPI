package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private String id;
    private String title;
    private String description;
    private Double amount;
    private String category;
    private LocalDateTime date;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}