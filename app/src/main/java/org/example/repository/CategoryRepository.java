package org.example.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.example.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class CategoryRepository {

    private static final String COLLECTION_NAME = "categories";

    @Autowired
    private Firestore firestore;

    public String save(Category category) throws ExecutionException, InterruptedException {
        if (category.getId() == null || category.getId().isEmpty()) {
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
            category.setId(docRef.getId());
        }
        
        ApiFuture<WriteResult> result = firestore.collection(COLLECTION_NAME)
                .document(category.getId()).set(category);
        result.get();
        return category.getId();
    }

    public Category findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            return document.toObject(Category.class);
        }
        return null;
    }

    public List<Category> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Category> categories = new ArrayList<>();
        
        for (QueryDocumentSnapshot document : documents) {
            Category category = document.toObject(Category.class);
            categories.add(category);
        }
        return categories;
    }

    public void deleteById(String id) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> writeResult = firestore.collection(COLLECTION_NAME).document(id).delete();
        writeResult.get();
    }

    public boolean existsById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        return document.exists();
    }
}