package org.example.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/demo")
@Tag(name = "🧪 Demo & Testing", description = "Demo endpoints for testing API functionality")
@CrossOrigin(origins = "*")
public class DemoController {

    @Operation(
        summary = "🏠 API Health Check",
        description = "Simple endpoint to verify API is running"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API is healthy and running")
    })
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "✅ Healthy");
        response.put("message", "Expense Tracker API is running successfully!");
        response.put("timestamp", System.currentTimeMillis());
        response.put("features", new String[]{
            "JWT Authentication",
            "OAuth2 Google Login", 
            "Expense Management",
            "Category System",
            "Statistics & Analytics",
            "Data Export (CSV/JSON)"
        });
        response.put("swaggerUI", "http://localhost:8080/swagger-ui.html");
        response.put("documentation", "See README.md and SWAGGER_DEMO_GUIDE.md");
        
        return response;
    }

    @Operation(
        summary = "📊 Sample Data Structure",
        description = "Returns sample JSON structures for testing API endpoints"
    )
    @GetMapping("/sample-data")
    public Map<String, Object> getSampleData() {
        Map<String, Object> response = new HashMap<>();
        
        // Sample user registration
        Map<String, String> sampleUser = new HashMap<>();
        sampleUser.put("email", "demo@expensetracker.com");
        sampleUser.put("password", "SecurePassword123!");
        sampleUser.put("firstName", "Demo");
        sampleUser.put("lastName", "User");
        
        // Sample category
        Map<String, String> sampleCategory = new HashMap<>();
        sampleCategory.put("name", "Food & Dining");
        sampleCategory.put("description", "Restaurant meals and food purchases");
        sampleCategory.put("color", "#FF6B6B");
        
        // Sample expense
        Map<String, Object> sampleExpense = new HashMap<>();
        sampleExpense.put("description", "Lunch at Italian restaurant");
        sampleExpense.put("amount", 35.50);
        sampleExpense.put("categoryId", "replace-with-actual-category-id");
        sampleExpense.put("date", "2024-01-20T12:30:00");
        
        response.put("userRegistration", sampleUser);
        response.put("category", sampleCategory);
        response.put("expense", sampleExpense);
        response.put("note", "Copy these samples and use them in Swagger UI endpoints");
        
        return response;
    }

    @Operation(
        summary = "🚀 Quick Start Guide",
        description = "Step-by-step instructions for using the API"
    )
    @GetMapping("/quick-start")
    public Map<String, Object> getQuickStartGuide() {
        Map<String, Object> response = new HashMap<>();
        
        String[] steps = {
            "1. 🔑 Register: POST /api/auth/register with sample user data",
            "2. 🔓 Login: POST /api/auth/login to get JWT token",
            "3. 🔒 Authorize: Click 'Authorize' button and enter 'Bearer <token>'",
            "4. 🏷️ Create Categories: POST /api/categories with sample data",
            "5. 💳 Add Expenses: POST /api/expenses using category IDs",
            "6. 📊 View Stats: GET /api/statistics/* endpoints",
            "7. 📤 Export Data: GET /api/export/* endpoints",
            "8. 🧪 Test OAuth2: GET /api/auth/oauth2/google for Google login"
        };
        
        response.put("steps", steps);
        response.put("swaggerUI", "http://localhost:8080/swagger-ui.html");
        response.put("demoGuide", "See SWAGGER_DEMO_GUIDE.md for detailed instructions");
        response.put("sampleData", "GET /api/demo/sample-data for copy-paste ready JSON");
        
        return response;
    }

    @Hidden
    @GetMapping("/test-oauth2")
    public String testOAuth2() {
        return "OAuth2 test endpoint - redirect to /oauth2/authorization/google";
    }
}