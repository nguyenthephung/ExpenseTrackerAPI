package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.AuthResponse;
import org.example.dto.LoginRequest;
import org.example.dto.RegisterRequest;
import org.example.model.User;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "🔐 Authentication", description = "User authentication and account management")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(
        summary = "🔑 User Login", 
        description = "Authenticate user with email and password to receive JWT token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(
                    name = "Successful Login",
                    summary = "JWT token response",
                    value = """
                    {
                        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyLWlkLTEyMyIsImlhdCI6MTY5...",
                        "type": "Bearer",
                        "id": "user-id-123",
                        "email": "john.doe@example.com",
                        "firstName": "John",
                        "lastName": "Doe",
                        "roles": ["USER"]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Login credentials",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequest.class),
                examples = @ExampleObject(
                    name = "Sample Login",
                    summary = "Login with email and password",
                    value = """
                    {
                        "email": "john.doe@example.com",
                        "password": "SecurePassword123!"
                    }
                    """
                )
            )
        )
        @Valid @RequestBody LoginRequest loginRequest) 
            throws ExecutionException, InterruptedException {
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(
        summary = "👤 User Registration", 
        description = "Create a new user account with email, password and personal information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "User registered successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Registration Success",
                    summary = "Successful registration response",
                    value = """
                    {
                        "message": "User registered successfully",
                        "userId": "user-id-456"
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid input or email already exists"),
        @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Registration information",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegisterRequest.class),
                examples = @ExampleObject(
                    name = "Sample Registration",
                    summary = "Register with full user information",
                    value = """
                    {
                        "email": "jane.smith@example.com",
                        "password": "SecurePassword123!",
                        "firstName": "Jane",
                        "lastName": "Smith"
                    }
                    """
                )
            )
        )
        @Valid @RequestBody RegisterRequest registerRequest) 
            throws ExecutionException, InterruptedException {
        String userId = authService.register(registerRequest);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("userId", userId);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
        summary = "👤 Get Current User", 
        description = "Retrieve current authenticated user profile information",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User profile retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = User.class),
                examples = @ExampleObject(
                    name = "User Profile",
                    summary = "Current user information",
                    value = """
                    {
                        "id": "user-id-123",
                        "email": "john.doe@example.com",
                        "firstName": "John",
                        "lastName": "Doe",
                        "roles": ["USER"],
                        "enabled": true,
                        "createdAt": "2024-01-15T10:30:00",
                        "lastLoginAt": "2024-01-20T14:25:00"
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "401", description = "Not authenticated"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            // Don't return password
            currentUser.setPassword(null);
            return ResponseEntity.ok(currentUser);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Logout current user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
}