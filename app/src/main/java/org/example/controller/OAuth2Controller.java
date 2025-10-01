package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth/oauth2")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "OAuth2 Authentication", description = "OAuth2 authentication endpoints")
public class OAuth2Controller {
    
    @Operation(
        summary = "Initiate Google OAuth2 login",
        description = "Redirects to Google OAuth2 authorization endpoint"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirect to Google OAuth2"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        // This endpoint will be handled by Spring Security OAuth2
        // Redirect to Google OAuth2 authorization endpoint
        response.sendRedirect("/oauth2/authorization/google");
    }
    
    @Operation(
        summary = "OAuth2 authentication success",
        description = "Handles successful OAuth2 authentication"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/success")
    public String success(
            @Parameter(description = "JWT token") 
            @RequestParam(required = false) String token,
            @Parameter(description = "Token type") 
            @RequestParam(required = false) String type) {
        if (token != null) {
            return "OAuth2 authentication successful! Token: " + token;
        }
        return "OAuth2 authentication successful!";
    }
    
    @Operation(
        summary = "OAuth2 authentication error",
        description = "Handles OAuth2 authentication errors"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Error information returned"),
        @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/error")
    public String error(
            @Parameter(description = "Error code") 
            @RequestParam(required = false) String error,
            @Parameter(description = "Error message") 
            @RequestParam(required = false) String message) {
        return "OAuth2 authentication failed. Error: " + error + 
               (message != null ? ", Message: " + message : "");
    }
}