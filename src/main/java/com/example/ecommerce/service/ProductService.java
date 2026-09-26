package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    public List<Product> findAllProducts() {

        return productRepository.findAll();
    }

    // Save product
    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    // Find product by ID
    public Product findProductById(Long id) {

        return productRepository.findById(id)
                .orElse(null);
    }

    // Edit product by ID
    public Product editById(Long id) {

        return productRepository.findById(id)
                .orElse(null);
    }

    // Delete product
    public void deleteProduct(Long id) {

        productRepository.deleteById(id);
    }

    // Search product
    public List<Product> searchProducts(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }

        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    // Find products by category
    public List<Product> findByCategory(Long categoryId) {

        return productRepository.findByCategoryId(categoryId);
    }

    // Search products by name and category
    public List<Product> searchByCategory(
            String keyword,
            Long categoryId) {

        return productRepository
                .findByNameContainingIgnoreCaseAndCategoryId(
                        keyword,
                        categoryId
                );
    }
}