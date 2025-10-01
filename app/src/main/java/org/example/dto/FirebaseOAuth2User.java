package org.example.dto;

import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.OAuth2User;
import org.example.util.FirebaseTimestampConverter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseOAuth2User {
    private String id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private String picture;
    private String provider;
    private String providerId;
    private boolean emailVerified;
    private List<String> roles;
    private boolean enabled;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLoginAt;
    
    public static FirebaseOAuth2User fromOAuth2User(OAuth2User oauth2User) {
        FirebaseOAuth2User firebaseUser = new FirebaseOAuth2User();
        firebaseUser.setId(oauth2User.getId());
        firebaseUser.setEmail(oauth2User.getEmail());
        firebaseUser.setName(oauth2User.getName());
        firebaseUser.setFirstName(oauth2User.getFirstName());
        firebaseUser.setLastName(oauth2User.getLastName());
        firebaseUser.setPicture(oauth2User.getPicture());
        firebaseUser.setProvider(oauth2User.getProvider());
        firebaseUser.setProviderId(oauth2User.getProviderId());
        firebaseUser.setEmailVerified(oauth2User.isEmailVerified());
        firebaseUser.setRoles(oauth2User.getRoles());
        firebaseUser.setEnabled(oauth2User.isEnabled());
        firebaseUser.setCreatedAt(FirebaseTimestampConverter.toTimestamp(oauth2User.getCreatedAt()));
        firebaseUser.setUpdatedAt(FirebaseTimestampConverter.toTimestamp(oauth2User.getUpdatedAt()));
        firebaseUser.setLastLoginAt(FirebaseTimestampConverter.toTimestamp(oauth2User.getLastLoginAt()));
        return firebaseUser;
    }
}