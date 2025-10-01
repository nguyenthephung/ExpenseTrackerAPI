package org.example.dto;

import com.google.cloud.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.User;
import org.example.util.FirebaseTimestampConverter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseUser {
    private String id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private boolean enabled;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLoginAt;
    
    public static FirebaseUser fromUser(User user) {
        FirebaseUser firebaseUser = new FirebaseUser();
        firebaseUser.setId(user.getId());
        firebaseUser.setUsername(user.getUsername());
        firebaseUser.setEmail(user.getEmail());
        firebaseUser.setPassword(user.getPassword());
        firebaseUser.setFirstName(user.getFirstName());
        firebaseUser.setLastName(user.getLastName());
        firebaseUser.setRoles(user.getRoles());
        firebaseUser.setEnabled(user.isEnabled());
        firebaseUser.setCreatedAt(FirebaseTimestampConverter.toTimestamp(user.getCreatedAt()));
        firebaseUser.setUpdatedAt(FirebaseTimestampConverter.toTimestamp(user.getUpdatedAt()));
        firebaseUser.setLastLoginAt(FirebaseTimestampConverter.toTimestamp(user.getLastLoginAt()));
        return firebaseUser;
    }
    
    public User toUser() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setRoles(this.roles);
        user.setEnabled(this.enabled);
        user.setCreatedAt(FirebaseTimestampConverter.toLocalDateTime(this.createdAt));
        user.setUpdatedAt(FirebaseTimestampConverter.toLocalDateTime(this.updatedAt));
        user.setLastLoginAt(FirebaseTimestampConverter.toLocalDateTime(this.lastLoginAt));
        return user;
    }
}