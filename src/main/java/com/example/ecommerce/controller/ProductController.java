package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    // ==========================
    // Show all products
    // ==========================
    @GetMapping("/products")
    public String showProducts(Model model) {

        List<Product> products =
                productService.findAllProducts();

        model.addAttribute("products", products);

        return "product";
    }

    // ==========================
    // Search products
    // ==========================
    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(required = false, defaultValue = "") String keyword,
            Model model) {

        List<Product> products =
                productService.searchProducts(keyword);

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);

        return "search-results";
    }

    // ==========================
    // Add product to cart
    // ==========================
    @PostMapping("/add-to-cart")
    public String addToCart(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {

        User user = getLoggedInUser(session);

        // User must login first
        if (user == null) {
            return "redirect:/login";
        }

        Product product =
                productService.findProductById(productId);

        if (product != null) {

            cartService.addToCart(
                    user,
                    productId,
                    quantity
            );
        }

        return "redirect:/cart";
    }

    // ==========================
    // Product details
    // ==========================
    @GetMapping("/product/{id}")
    public String viewProductDetails(
            @PathVariable Long id,
            Model model) {

        Product product =
                productService.findProductById(id);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute(
                "product",
                product
        );

        return "product-details";
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