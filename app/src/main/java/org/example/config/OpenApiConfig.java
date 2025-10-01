package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI expenseTrackerOpenAPI() {
        final String jwtSecurityScheme = "bearerAuth";
        final String oauth2SecurityScheme = "oauth2";
        
        return new OpenAPI()
                .servers(List.of(
                    new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Development server")
                ))
                .addSecurityItem(new SecurityRequirement()
                    .addList(jwtSecurityScheme)
                    .addList(oauth2SecurityScheme))
                .components(
                    new Components()
                        .addSecuritySchemes(jwtSecurityScheme,
                            new SecurityScheme()
                                .name(jwtSecurityScheme)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Authentication: Login to get token, then use 'Bearer <token>' in Authorization header")
                        )
                        .addSecuritySchemes(oauth2SecurityScheme,
                            new SecurityScheme()
                                .name(oauth2SecurityScheme)
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("OAuth2 Google Authentication")
                                .flows(new OAuthFlows()
                                    .authorizationCode(new OAuthFlow()
                                        .authorizationUrl("http://localhost:" + serverPort + "/oauth2/authorization/google")
                                        .tokenUrl("https://oauth2.googleapis.com/token")
                                    )
                                )
                        )
                )
                .info(new Info()
                        .title("💰 Expense Tracker API")
                        .description("""
                            ## 🚀 Comprehensive Personal Finance Management API
                            
                            A full-featured REST API for tracking personal expenses with:
                            
                            ### 🔐 Authentication Options
                            - **JWT Authentication**: Traditional email/password login
                            - **OAuth2 Google**: Sign in with Google account
                            
                            ### 📊 Core Features  
                            - **Expense Management**: Create, read, update, delete expenses
                            - **Category System**: Organize expenses by categories
                            - **Statistics & Analytics**: Monthly/yearly spending reports
                            - **Data Export**: CSV/JSON export functionality
                            - **Firebase Integration**: Cloud-based data storage
                            
                            ### 🛠️ Technology Stack
                            - **Backend**: Spring Boot 3.1.4 + Java 17
                            - **Database**: Firebase Firestore (NoSQL)
                            - **Security**: Spring Security + JWT + OAuth2
                            - **Documentation**: OpenAPI 3.0 (Swagger)
                            
                            ### 📖 How to Use with Swagger UI
                            1. **Authentication**: Start with `/api/auth/login` or `/api/auth/oauth2/google`
                            2. **Authorization**: Copy the token and click 'Authorize' button above
                            3. **Testing**: All endpoints are now accessible for testing
                            
                            **Note**: Most endpoints require authentication. Please login first!
                            """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Expense Tracker Development Team")
                                .email("dev@expensetracker.com")
                                .url("https://github.com/nguyenthephung/ExpenseTrackerAPI"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}