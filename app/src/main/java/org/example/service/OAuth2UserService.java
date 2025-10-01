package org.example.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.AuthResponse;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService {
    
    private final UserRepository userRepository;
    private final Firestore firestore;
    
    public AuthResponse processOAuth2User(OAuth2User oauth2User) throws ExecutionException, InterruptedException {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        String providerId = oauth2User.getAttribute("sub");
        Boolean emailVerified = oauth2User.getAttribute("email_verified");
        
        log.info("Processing OAuth2 user: {}", email);
        
        // Check if user already exists
        Optional<User> existingUser = findUserByEmail(email);
        
        if (existingUser.isPresent()) {
            // Update existing user
            User user = existingUser.get();
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
            
            return new AuthResponse(
                    null, "Bearer", user.getId(), user.getEmail(), 
                    user.getFirstName(), user.getLastName(), user.getRoles()
            );
        } else {
            // Create new user from OAuth2 data
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(oauth2User.getAttribute("given_name"));
            newUser.setLastName(oauth2User.getAttribute("family_name"));
            newUser.setRoles(List.of("USER"));
            newUser.setEnabled(emailVerified != null ? emailVerified : true);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            newUser.setLastLoginAt(LocalDateTime.now());
            
            // Save new user
            String savedUserId = userRepository.save(newUser);
            newUser.setId(savedUserId);
            
            // Also save OAuth2 specific information
            saveOAuth2User(savedUserId, oauth2User, "google");
            
            return new AuthResponse(
                    null, "Bearer", newUser.getId(), newUser.getEmail(), 
                    newUser.getFirstName(), newUser.getLastName(), newUser.getRoles()
            );
        }
    }
    
    private Optional<User> findUserByEmail(String email) throws ExecutionException, InterruptedException {
        QuerySnapshot querySnapshot = firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .get();
        
        if (!querySnapshot.isEmpty()) {
            User user = querySnapshot.getDocuments().get(0).toObject(User.class);
            user.setId(querySnapshot.getDocuments().get(0).getId());
            return Optional.of(user);
        }
        
        return Optional.empty();
    }
    
    private void saveOAuth2User(String userId, OAuth2User oauth2User, String provider) {
        try {
            org.example.model.OAuth2User oAuth2UserEntity = new org.example.model.OAuth2User();
            oAuth2UserEntity.setId(userId + "_" + provider);
            oAuth2UserEntity.setEmail(oauth2User.getAttribute("email"));
            oAuth2UserEntity.setName(oauth2User.getAttribute("name"));
            oAuth2UserEntity.setFirstName(oauth2User.getAttribute("given_name"));
            oAuth2UserEntity.setLastName(oauth2User.getAttribute("family_name"));
            oAuth2UserEntity.setPicture(oauth2User.getAttribute("picture"));
            oAuth2UserEntity.setProvider(provider);
            oAuth2UserEntity.setProviderId(oauth2User.getAttribute("sub"));
            oAuth2UserEntity.setEmailVerified(oauth2User.getAttribute("email_verified"));
            oAuth2UserEntity.setRoles(List.of("USER"));
            oAuth2UserEntity.setEnabled(true);
            oAuth2UserEntity.setCreatedAt(LocalDateTime.now());
            oAuth2UserEntity.setUpdatedAt(LocalDateTime.now());
            oAuth2UserEntity.setLastLoginAt(LocalDateTime.now());
            
            firestore.collection("oauth2_users")
                    .document(oAuth2UserEntity.getId())
                    .set(oAuth2UserEntity)
                    .get();
            
            log.info("OAuth2 user saved: {}", oAuth2UserEntity.getEmail());
        } catch (Exception e) {
            log.error("Error saving OAuth2 user: {}", e.getMessage());
        }
    }
}