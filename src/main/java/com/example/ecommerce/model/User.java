package com.example.ecommerce.model;

import com.example.ecommerce.Validation.PasswordMatcher;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@PasswordMatcher
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(
        min = 3,
        max = 30,
        message = "Username must be between 3 and 30 characters"
    )
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(
        min = 6,
        message = "Password must contain at least 6 characters"
    )
    @Column(nullable = false)
    private String password;

    /*
     * Confirm password is only used during validation.
     * It will NOT be stored in the database.
     */
    @Transient
    @NotBlank(message = "Confirm password is required")
    private String confirmpassword;
    
    @Column(name = "profile_image")
    private String profileImage;
    
    @Column(name = "phone")
    private String phone;

    @Column(nullable = false)
    private String role = "USER";

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    // Default constructor
    public User() {
    }

    // Constructor
    public User(
            String username,
            String email,
            String password,
            String role) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Confirm Password
    public String getConfirmpassword() {
        return confirmpassword;
    }

    public void setConfirmpassword(String confirmpassword) {
        this.confirmpassword = confirmpassword;
    }

    // Role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Orders
    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    
 // Profile Image

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    // Cart
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
    
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /*
     * Check whether password and confirm password match.
     */
    public boolean isPasswordMatching() {

        if (password == null || confirmpassword == null) {
            return false;
        }

        return password.equals(confirmpassword);
    }
}