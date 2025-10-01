package org.example.security;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsernameOrEmail(usernameOrEmail);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
            }
            return UserPrincipal.create(user);
        } catch (ExecutionException | InterruptedException e) {
            throw new UsernameNotFoundException("Error loading user: " + usernameOrEmail, e);
        }
    }

    public UserDetails loadUserById(String id) {
        try {
            User user = userRepository.findById(id);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with id: " + id);
            }
            return UserPrincipal.create(user);
        } catch (ExecutionException | InterruptedException e) {
            throw new UsernameNotFoundException("Error loading user with id: " + id, e);
        }
    }
}