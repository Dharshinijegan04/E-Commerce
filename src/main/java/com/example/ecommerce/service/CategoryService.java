package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Get all categories
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    // Save category
    public Category saveCategory(Category category) {

        return categoryRepository.save(category);
    }

    // Find category by ID
    public Category findCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElse(null);
    }

    // Find category by name
    public Category findByName(String name) {

        return categoryRepository.findByName(name)
                .orElse(null);
    }

    // Delete category
    public void deleteCategory(Long id) {

        categoryRepository.deleteById(id);
    }
}