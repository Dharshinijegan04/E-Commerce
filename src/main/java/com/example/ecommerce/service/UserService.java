package com.example.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register user
    public List<String> registerUser(User user, BindingResult bindingResult) {

        List<String> errors = new ArrayList<>();

        // Check username
        if (userRepository.existsByUsername(user.getUsername())) {
            errors.add("Username already exists");
        }

        // Check email
        if (userRepository.existsByEmail(user.getEmail())) {
            errors.add("Email already exists");
        }

        // Check validation errors
        if (bindingResult.hasErrors()) {

            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
        }

        // Save only when there are no errors
        if (errors.isEmpty()) {

            // Default role
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }

            userRepository.save(user);
        }

        return errors;
    }

    // Login user
    public User loginUser(String username, String password) {

        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }

    // Find user by username
    public User findByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElse(null);
    }

    // Find user by ID
    public User findById(Long id) {

        return userRepository.findById(id)
                .orElse(null);
    }

    // Get all users
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    // Delete user
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }
}