package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProductService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Redirect /admin -> login
    @GetMapping
    public String redirectToLogin() {
        return "redirect:/admin/login";
    }

    // Admin login page
    @GetMapping("/login")
    public String showAdminLogin() {
        return "admin/adminLogin";
    }

    // Admin login validation
    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        // Simple admin login for now
        if ("Admin".equals(username)
                && "Admin@123".equals(password)) {

            return "redirect:/admin/products";
        }

        model.addAttribute("error",
                "Invalid username or password");

        return "admin/adminLogin";
    }

    // Show all products
    @GetMapping("/products")
    public String showProducts(Model model) {

        List<Product> products =
                productService.findAllProducts();

        model.addAttribute("products", products);

        return "admin/products";
    }

    // Add product page
    @GetMapping("/products/add")
    public String addProductPage(Model model) {

        model.addAttribute("product", new Product());

        List<Category> categories =
                categoryService.getAllCategories();

        model.addAttribute("categories", categories);

        return "admin/add-products";
    }

    // Save product
    @PostMapping("/products/add")
    public String saveProduct(
            @ModelAttribute Product product) {

        productService.saveProduct(product);

        return "redirect:/admin/products";
    }

    // Edit product page
    @GetMapping("/products/edit/{id}")
    public String editProductPage(
            @PathVariable Long id,
            Model model) {

        Product product =
                productService.findProductById(id);

        if (product == null) {
            return "redirect:/admin/products";
        }

        model.addAttribute("product", product);

        List<Category> categories =
                categoryService.getAllCategories();

        model.addAttribute("categories", categories);

        return "admin/edit-products";
    }

    // Update product
    @PostMapping("/products/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute Product product) {

        product.setId(id);

        productService.saveProduct(product);

        return "redirect:/admin/products";
    }

    // Delete product
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return "redirect:/admin/products";
    }
}