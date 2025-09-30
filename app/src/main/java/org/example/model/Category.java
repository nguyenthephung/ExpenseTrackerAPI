package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private String id;
    private String name;
    private String description;
    private String color; // Hex color code for UI
    private String icon; // Icon name or emoji
}