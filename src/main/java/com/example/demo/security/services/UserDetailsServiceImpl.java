package com.example.demo.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service 
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired 
    UserRepository userRepository;

    @Override
    @Transactional 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // This method can still be used for loading by username
        return loadUserByIdentifier(username);
    }

    // New method to load user by identifier (username or email)
    @Transactional
    public UserDetails loadUserByIdentifier(String identifier) throws UsernameNotFoundException {
        User user;

        // Check if the identifier is an email or username
        if (isValidEmail(identifier)) {
            user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User  Not Found with email: " + identifier));
        } else {
            user = userRepository.findByUsername(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User  Not Found with username: " + identifier));
        }

        return UserDetailsImpl.build(user); // Build UserDetailsImpl with the user object
    }

    // Helper method to validate email format
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

}