package com.example.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "products")
public class Product {

    // ==========================
    // Primary Key
    // ==========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // ==========================
    // Product Name
    // ==========================

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;


    // ==========================
    // Product Image
    // ==========================

    @Column(length = 1000)
    private String imageUrl;


    // ==========================
    // Product Price
    // ==========================

    @Positive(message = "Price must be greater than zero")
    @Column(nullable = false)
    private double price;


    // ==========================
    // Product Description
    // ==========================

    @Column(length = 2000)
    private String description;


    // ==========================
    // Product Stock
    // ==========================

    @Min(value = 0, message = "Stock cannot be negative")
    @Column(nullable = false)
    private int stock;


    // ==========================
    // Product Category
    // ==========================

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    // ==========================
    // Default Constructor
    // ==========================

    public Product() {
    }


    // ==========================
    // Parameterized Constructor
    // ==========================

    public Product(
            String name,
            String imageUrl,
            double price,
            String description,
            int stock,
            Category category) {

        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.category = category;
    }


    // ==========================
    // Getters and Setters
    // ==========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}