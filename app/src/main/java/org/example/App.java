package org.example;

import org.example.config.DotenvConfig;
import org.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    private CategoryService categoryService;

    @Value("${server.port}")
    private String serverPort;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.addInitializers(new DotenvConfig());
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize default categories on startup
        categoryService.initializeDefaultCategories();
        System.out.println("🚀 Expense Tracker API started successfully!");
        System.out.println("📖 Swagger UI: http://localhost:" + serverPort + "/swagger-ui.html");
        System.out.println("📋 API Docs: http://localhost:" + serverPort + "/api-docs");
        System.out.println("🔐 Authentication endpoints:");
        System.out.println("   POST /api/auth/register - Register new user");
        System.out.println("   POST /api/auth/login - Login user");
        System.out.println("   GET /api/auth/me - Get current user");
    }
}
