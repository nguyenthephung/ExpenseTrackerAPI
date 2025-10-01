package org.example.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.example.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ExpenseRepository {

    private static final String COLLECTION_NAME = "expenses";

    @Autowired
    private Firestore firestore;

    public String save(Expense expense) throws ExecutionException, InterruptedException {
        if (expense.getId() == null || expense.getId().isEmpty()) {
            // Create new document
            DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
            expense.setId(docRef.getId());
            expense.setCreatedAt(LocalDateTime.now());
        }
        expense.setUpdatedAt(LocalDateTime.now());
        
        ApiFuture<WriteResult> result = firestore.collection(COLLECTION_NAME)
                .document(expense.getId()).set(expense);
        result.get();
        return expense.getId();
    }

    public Expense findById(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        
        if (document.exists()) {
            return document.toObject(Expense.class);
        }
        return null;
    }

    public List<Expense> findAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Expense> expenses = new ArrayList<>();
        
        for (QueryDocumentSnapshot document : documents) {
            Expense expense = document.toObject(Expense.class);
            expenses.add(expense);
        }
        return expenses;
    }

    public List<Expense> findByUserId(String userId) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME).whereEqualTo("userId", userId);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Expense> expenses = new ArrayList<>();
        
        for (QueryDocumentSnapshot document : documents) {
            Expense expense = document.toObject(Expense.class);
            expenses.add(expense);
        }
        return expenses;
    }

    public List<Expense> findByCategory(String category) throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME).whereEqualTo("category", category);
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Expense> expenses = new ArrayList<>();
        
        for (QueryDocumentSnapshot document : documents) {
            Expense expense = document.toObject(Expense.class);
            expenses.add(expense);
        }
        return expenses;
    }

    public List<Expense> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) 
            throws ExecutionException, InterruptedException {
        Query query = firestore.collection(COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate)
                .orderBy("date", Query.Direction.DESCENDING);
        
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Expense> expenses = new ArrayList<>();
        
        for (QueryDocumentSnapshot document : documents) {
            Expense expense = document.toObject(Expense.class);
            expenses.add(expense);
        }
        return expenses;
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