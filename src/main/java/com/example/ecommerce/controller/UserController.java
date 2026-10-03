package com.example.ecommerce.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;


    // ==========================
    // Home page
    // ==========================
    @GetMapping("/")
    public String front() {
        return "front";
    }


    // ==========================
    // Home
    // ==========================
    @GetMapping("/home")
    public String home() {
        return "home";
    }


    // ==========================
    // Shop
    // ==========================
    @GetMapping("/shop")
    public String getShopPage(Model model) {

        List<Product> products =
                productService.findAllProducts();

        model.addAttribute(
                "products",
                products
        );

        return "shop";
    }


    // ==========================
    // User home
    // ==========================
    @GetMapping("/user")
    public String userHome(Model model) {

        List<Product> products =
                productService.findAllProducts();

        model.addAttribute(
                "products",
                products
        );

        return "product";
    }


    // ==========================
    // Login page
    // ==========================
    @GetMapping("/login")
    public String login() {
        return "login";
    }


    // ==========================
    // Registration page
    // ==========================
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute(
                "user",
                new User()
        );

        return "register";
    }


    // ==========================
    // Register user
    // ==========================
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {

        List<String> errors =
                userService.registerUser(
                        user,
                        result
                );

        if (!errors.isEmpty()) {

            model.addAttribute(
                    "errors",
                    errors
            );

            return "register";
        }

        return "redirect:/login";
    }


    // ==========================
    // Login user
    // ==========================
    @PostMapping("/login")
    public String loginUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        User user =
                userService.loginUser(
                        username,
                        password
                );

        if (user != null) {

            // Store username in session
            session.setAttribute(
                    "username",
                    user.getUsername()
            );

            // Store user object in session
            session.setAttribute(
                    "user",
                    user
            );

            // After successful login
            return "redirect:/home";
        }

        model.addAttribute(
                "error",
                "Invalid username or password"
        );

        return "login";
    }


    // ==========================
    // Wishlist page
    // ==========================
    @GetMapping("/wishlist")
    public String wishlist() {
        return "wishlist";
    }


    // ==========================
    // Order history
    // ==========================
    @GetMapping("/order-history")
    public String orderHistory() {
        return "redirect:/orders";
    }


    // ==========================
    // Profile
    // ==========================
    @GetMapping("/profile")
    public String profile(
            HttpSession session,
            Model model) {

        User user =
                getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "user",
                user
        );

        return "profile";
    }


    // ==========================
    // Edit profile
    // ==========================
    @GetMapping("/edit-profile")
    public String editProfile(
            HttpSession session,
            Model model) {

        User user =
                getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute(
                "user",
                user
        );

        return "edit-profile";
    }


    // ==========================
    // Update profile
    // ==========================
    @PostMapping("/update-profile")
    public String updateProfile(
            @RequestParam("username") String username,
            @RequestParam("phone") String phone,
            @RequestParam(
                    value = "profileImage",
                    required = false
            ) MultipartFile profileImage,
            HttpSession session) throws IOException {

        // Get logged-in user
        User user =
                getLoggedInUser(session);

        // If user is not logged in
        if (user == null) {
            return "redirect:/login";
        }

        // ==========================
        // Update username
        // ==========================
        user.setUsername(username);


        // ==========================
        // Update phone number
        // ==========================
        user.setPhone(phone);


        // ==========================
        // Upload profile image
        // ==========================
        if (profileImage != null
                && !profileImage.isEmpty()) {

            String uploadDir =
                    "uploads/profile/";

            // Create directory if it doesn't exist
            File directory =
                    new File(uploadDir);

            if (!directory.exists()) {
                directory.mkdirs();
            }


            // Get original file name
            String originalFileName =
                    profileImage.getOriginalFilename();

            if (originalFileName == null) {
                originalFileName = "profile-image";
            }


            // Create unique file name
            String fileName =
                    System.currentTimeMillis()
                    + "_"
                    + originalFileName;


            // Create file path
            Path filePath =
                    Paths.get(
                            uploadDir + fileName
                    );


            // Save image
            Files.write(
                    filePath,
                    profileImage.getBytes()
            );


            // Store image URL in database
            user.setProfileImage(
                    "/uploads/profile/"
                    + fileName
            );
        }


        // ==========================
        // Save updated user
        // ==========================
        userService.saveUser(user);


        // ==========================
        // Update session
        // ==========================
        session.setAttribute(
                "user",
                user
        );

        session.setAttribute(
                "username",
                user.getUsername()
        );


        // ==========================
        // Return to profile
        // ==========================
        return "redirect:/profile";
    }


    // ==========================
    // About
    // ==========================
    @GetMapping("/about")
    public String about(
            HttpSession session) {

        User user =
                (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        return "about";
    }


    // ==========================
    // Contact
    // ==========================
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }


    // ==========================
    // FAQ
    // ==========================
    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }


    // ==========================
    // Logout
    // ==========================
    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }


    // ==========================
    // Get logged-in user
    // ==========================
    private User getLoggedInUser(
            HttpSession session) {

        Object usernameObject =
                session.getAttribute("username");

        if (usernameObject == null) {
            return null;
        }

        String username =
                usernameObject.toString();

        return userService.findByUsername(username);
    }
}