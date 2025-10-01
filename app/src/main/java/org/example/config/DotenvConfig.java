package org.example.config;

import me.paulschwarz.springdotenv.environment.DotenvPropertySource;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        
        // Try to load .env file from different locations
        Resource[] possibleLocations = {
            new FileSystemResource(".env"),
            new FileSystemResource("../.env"),
            new ClassPathResource(".env")
        };
        
        for (Resource resource : possibleLocations) {
            if (resource.exists()) {
                try {
                    DotenvPropertySource propertySource = new DotenvPropertySource(resource);
                    environment.getPropertySources().addLast(propertySource);
                    System.out.println("✅ Loaded .env file from: " + resource.getDescription());
                    break;
                } catch (IOException e) {
                    System.out.println("❌ Failed to load .env file from: " + resource.getDescription());
                }
            }
        }
    }
}