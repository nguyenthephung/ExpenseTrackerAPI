package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2User {
    private String id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private String picture;
    private String provider; // google, facebook, etc.
    private String providerId; // ID from OAuth provider
    private boolean emailVerified;
    private List<String> roles;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
}