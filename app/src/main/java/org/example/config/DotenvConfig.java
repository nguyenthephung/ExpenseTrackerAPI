package org.example.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        
        Map<String, Object> envProperties = loadDotenvFile();
        if (!envProperties.isEmpty()) {
            MapPropertySource propertySource = new MapPropertySource("dotenv", envProperties);
            environment.getPropertySources().addLast(propertySource);
            System.out.println("✅ Loaded .env file with " + envProperties.size() + " properties");
        } else {
            System.out.println("⚠️ No .env file found or empty. Using default values.");
        }
    }

    private Map<String, Object> loadDotenvFile() {
        Map<String, Object> properties = new HashMap<>();
        
        // Try different locations for .env file
        String[] possiblePaths = {
            ".env",
            "../.env",
            "../../.env"
        };
        
        for (String pathStr : possiblePaths) {
            Path path = Paths.get(pathStr);
            if (Files.exists(path)) {
                try {
                    properties = parseDotenvFile(path);
                    System.out.println("📁 Found .env file at: " + path.toAbsolutePath());
                    break;
                } catch (IOException e) {
                    System.out.println("❌ Error reading .env file: " + e.getMessage());
                }
            }
        }
        
        // Also try to load from classpath
        if (properties.isEmpty()) {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(".env")) {
                if (is != null) {
                    properties = parseDotenvInputStream(is);
                    System.out.println("📁 Found .env file in classpath");
                }
            } catch (IOException e) {
                System.out.println("❌ Error reading .env from classpath: " + e.getMessage());
            }
        }
        
        return properties;
    }

    private Map<String, Object> parseDotenvFile(Path path) throws IOException {
        Map<String, Object> properties = new HashMap<>();
        
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                
                int equalIndex = line.indexOf('=');
                if (equalIndex > 0) {
                    String key = line.substring(0, equalIndex).trim();
                    String value = line.substring(equalIndex + 1).trim();
                    
                    // Remove quotes if present
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    properties.put(key, value);
                }
            }
        }
        
        return properties;
    }

    private Map<String, Object> parseDotenvInputStream(InputStream inputStream) throws IOException {
        Map<String, Object> properties = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                int equalIndex = line.indexOf('=');
                if (equalIndex > 0) {
                    String key = line.substring(0, equalIndex).trim();
                    String value = line.substring(equalIndex + 1).trim();
                    
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        value = value.substring(1, value.length() - 1);
                    }
                    
                    properties.put(key, value);
                }
            }
        }
        
        return properties;
    }
}