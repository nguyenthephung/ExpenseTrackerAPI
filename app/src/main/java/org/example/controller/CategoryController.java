package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.model.Category;
import org.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category Management", description = "APIs for managing expense categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates a new expense category")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) 
            throws ExecutionException, InterruptedException {
        Category response = categoryService.createCategory(category);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "Updates an existing category by ID")
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "Category ID") @PathVariable String id,
            @Valid @RequestBody Category category) throws ExecutionException, InterruptedException {
        Category response = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieves a specific category by its ID")
    public ResponseEntity<Category> getCategoryById(
            @Parameter(description = "Category ID") @PathVariable String id) 
            throws ExecutionException, InterruptedException {
        Category response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieves all expense categories")
    public ResponseEntity<List<Category>> getAllCategories() 
            throws ExecutionException, InterruptedException {
        List<Category> responses = categoryService.getAllCategories();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "Deletes a category by ID")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "Category ID") @PathVariable String id) 
            throws ExecutionException, InterruptedException {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/initialize")
    @Operation(summary = "Initialize default categories", description = "Creates default expense categories if none exist")
    public ResponseEntity<String> initializeDefaultCategories() 
            throws ExecutionException, InterruptedException {
        categoryService.initializeDefaultCategories();
        return ResponseEntity.ok("Default categories initialized successfully");
    }
}