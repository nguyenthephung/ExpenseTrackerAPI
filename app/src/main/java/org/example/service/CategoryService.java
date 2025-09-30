package org.example.service;

import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) throws ExecutionException, InterruptedException {
        String id = categoryRepository.save(category);
        return categoryRepository.findById(id);
    }

    public Category updateCategory(String id, Category category) throws ExecutionException, InterruptedException {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        category.setId(id);
        categoryRepository.save(category);
        return category;
    }

    public Category getCategoryById(String id) throws ExecutionException, InterruptedException {
        Category category = categoryRepository.findById(id);
        if (category == null) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        return category;
    }

    public List<Category> getAllCategories() throws ExecutionException, InterruptedException {
        return categoryRepository.findAll();
    }

    public void deleteCategory(String id) throws ExecutionException, InterruptedException {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    public void initializeDefaultCategories() throws ExecutionException, InterruptedException {
        List<Category> existingCategories = categoryRepository.findAll();
        if (existingCategories.isEmpty()) {
            List<Category> defaultCategories = Arrays.asList(
                    new Category(null, "Food & Dining", "Meals, restaurants, groceries", "#FF6B6B", "🍽️"),
                    new Category(null, "Transportation", "Gas, public transport, taxi", "#4ECDC4", "🚗"),
                    new Category(null, "Shopping", "Clothes, electronics, general shopping", "#45B7D1", "🛒"),
                    new Category(null, "Entertainment", "Movies, games, sports", "#96CEB4", "🎬"),
                    new Category(null, "Bills & Utilities", "Electricity, water, internet, phone", "#FECA57", "💡"),
                    new Category(null, "Healthcare", "Medical, pharmacy, insurance", "#FF9FF3", "🏥"),
                    new Category(null, "Education", "Books, courses, school fees", "#A8E6CF", "📚"),
                    new Category(null, "Travel", "Vacation, hotels, flights", "#FFD93D", "✈️"),
                    new Category(null, "Personal Care", "Haircut, cosmetics, gym", "#6C5CE7", "💄"),
                    new Category(null, "Other", "Miscellaneous expenses", "#95A5A6", "📝")
            );

            for (Category category : defaultCategories) {
                categoryRepository.save(category);
            }
        }
    }
}