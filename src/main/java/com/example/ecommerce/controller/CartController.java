package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    // View Cart
    @GetMapping("/cart")
    public String viewCart(
            HttpSession session,
            Model model) {

        User user = getLoggedInUser(session);

        // User not logged in
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getCart(user);

        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("totalAmount",
                cart.getTotalAmount());

        return "cart";
    }

    // Add product to cart
    @GetMapping("/cart/add/{id}")
    public String addToCart(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        cartService.addToCart(user, id, quantity);

        return "redirect:/cart";
    }

    // Remove item from cart
    @GetMapping("/cart/remove/{id}")
    public String removeFromCart(
            @PathVariable Long id,
            HttpSession session) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        cartService.removeFromCart(user, id);

        return "redirect:/cart";
    }

    // Clear cart
    @GetMapping("/cart/clear")
    public String clearCart(HttpSession session) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        cartService.clearCart(user);

        return "redirect:/cart";
    }

    // Get logged-in user from session
    private User getLoggedInUser(HttpSession session) {

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