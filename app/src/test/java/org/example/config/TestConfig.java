package org.example.config;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.example.repository.CategoryRepository;
import org.example.repository.ExpenseRepository;
import org.example.repository.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestConfiguration
@Profile("test")
public class TestConfig {
    
    @Bean
    @Primary
    public Firestore firestore() throws ExecutionException, InterruptedException {
        Firestore mockFirestore = Mockito.mock(Firestore.class);
        CollectionReference mockCollection = Mockito.mock(CollectionReference.class);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        ApiFuture<QuerySnapshot> mockFuture = Mockito.mock(ApiFuture.class);
        QuerySnapshot mockQuerySnapshot = Mockito.mock(QuerySnapshot.class);
        ApiFuture<WriteResult> mockWriteResult = Mockito.mock(ApiFuture.class);
        ApiFuture<DocumentSnapshot> mockDocFuture = Mockito.mock(ApiFuture.class);
        DocumentSnapshot mockDocSnapshot = Mockito.mock(DocumentSnapshot.class);
        WriteResult mockWrite = Mockito.mock(WriteResult.class);
        
        // Mock collection behavior
        when(mockFirestore.collection(anyString())).thenReturn(mockCollection);
        when(mockCollection.document()).thenReturn(mockDocument);
        when(mockCollection.document(anyString())).thenReturn(mockDocument);
        when(mockCollection.get()).thenReturn(mockFuture);
        when(mockCollection.whereEqualTo(anyString(), any())).thenReturn(Mockito.mock(Query.class));
        
        // Mock document behavior
        when(mockDocument.getId()).thenReturn("test-id");
        when(mockDocument.set(any())).thenReturn(mockWriteResult);
        when(mockDocument.get()).thenReturn(mockDocFuture);
        when(mockDocument.delete()).thenReturn(mockWriteResult);
        
        // Mock futures
        when(mockFuture.get()).thenReturn(mockQuerySnapshot);
        when(mockWriteResult.get()).thenReturn(mockWrite);
        when(mockDocFuture.get()).thenReturn(mockDocSnapshot);
        
        // Mock query results
        when(mockQuerySnapshot.getDocuments()).thenReturn(new ArrayList<>());
        when(mockDocSnapshot.exists()).thenReturn(false);
        
        return mockFirestore;
    }
    
    @Bean
    @Primary
    public CategoryRepository categoryRepository() throws ExecutionException, InterruptedException {
        CategoryRepository mockRepo = Mockito.mock(CategoryRepository.class);
        when(mockRepo.findAll()).thenReturn(new ArrayList<>());
        return mockRepo;
    }
    
    @Bean
    @Primary 
    public ExpenseRepository expenseRepository() {
        return Mockito.mock(ExpenseRepository.class);
    }
    
    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }
}