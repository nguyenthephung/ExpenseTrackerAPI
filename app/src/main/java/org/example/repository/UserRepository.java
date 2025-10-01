package org.example.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.example.dto.FirebaseUser;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {

    private static final String COLLECTION_NAME = "users";

    @Autowired
    private Firestore firestore;

    public String save(User user) throws ExecutionException, InterruptedException {
        if (user.getId() == null || user.getId().isEmpty()) {
            // Create new document
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
            user.setId(docRef.getId());
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        
        // Convert to FirebaseUser to handle Timestamp conversion
        FirebaseUser firebaseUser = FirebaseUser.fromUser(user);
        
        ApiFuture<WriteResult> result = firestore.collection(COLLECTION_NAME)
                .document(user.getId()).set(firebaseUser);
        result.get();
        return user.getId();
    }

    public User findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            FirebaseUser firebaseUser = document.toObject(FirebaseUser.class);
            return firebaseUser != null ? firebaseUser.toUser() : null;
        }
        return null;
    }

    public User findByUsername(String username) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME).whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        if (!documents.isEmpty()) {
            FirebaseUser firebaseUser = documents.get(0).toObject(FirebaseUser.class);
            return firebaseUser != null ? firebaseUser.toUser() : null;
        }
        return null;
    }

    public User findByEmail(String email) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME).whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        if (!documents.isEmpty()) {
            FirebaseUser firebaseUser = documents.get(0).toObject(FirebaseUser.class);
            return firebaseUser != null ? firebaseUser.toUser() : null;
        }
        return null;
    }

    public User findByUsernameOrEmail(String usernameOrEmail) throws ExecutionException, InterruptedException {
        User user = findByUsername(usernameOrEmail);
        if (user == null) {
            user = findByEmail(usernameOrEmail);
        }
        return user;
    }

    public boolean existsByUsername(String username) throws ExecutionException, InterruptedException {
        return findByUsername(username) != null;
    }

    public boolean existsByEmail(String email) throws ExecutionException, InterruptedException {
        return findByEmail(email) != null;
    }

    public List<User> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<User> users = new ArrayList<>();
        
        for (QueryDocumentSnapshot document : documents) {
            FirebaseUser firebaseUser = document.toObject(FirebaseUser.class);
            if (firebaseUser != null) {
                users.add(firebaseUser.toUser());
            }
        }
        return users;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = firestore.collection(COLLECTION_NAME).document(id).delete();
        writeResult.get();
    }
}