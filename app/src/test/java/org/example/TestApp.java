package org.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("test")
public class TestApp {
    // Test application without CommandLineRunner
    // This prevents initialization of default categories during tests
}