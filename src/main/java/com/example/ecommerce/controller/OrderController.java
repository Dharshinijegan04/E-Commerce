package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // ==========================
    // Checkout page
    // ==========================
    @GetMapping("/checkout")
    public String checkout(
            HttpSession session,
            Model model) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getCart(user);

        if (cart.getItems() == null ||
                cart.getItems().isEmpty()) {

            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute(
                "totalAmount",
                cart.getTotalAmount()
        );

        return "checkout";
    }

    // ==========================
    // Place order
    // ==========================
    @PostMapping("/place-order")
    public String placeOrder(
            HttpSession session,
            Model model) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getCart(user);

        if (cart.getItems() == null ||
                cart.getItems().isEmpty()) {

            return "redirect:/cart";
        }

        // Create new order
        Order order = new Order();

        order.setUser(user);
        order.setTotalAmount(
                cart.getTotalAmount()
        );

        // Cash on Delivery
        order.setPaymentMethod("COD");
        order.setPaymentStatus("PENDING");
        order.setStatus("PLACED");

        // Copy cart items to order items
        for (CartItem cartItem : cart.getItems()) {

            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(
                    cartItem.getProduct()
            );

            orderItem.setQuantity(
                    cartItem.getQuantity()
            );

            orderItem.setPrice(
                    cartItem.getProduct().getPrice()
            );

            order.addItem(orderItem);
        }

        // Save order
        Order savedOrder =
                orderService.saveOrder(order);

        // Clear cart
        cartService.clearCart(user);

        // Send order to success page
        model.addAttribute(
                "order",
                savedOrder
        );

        return "order-success";
    }

    // ==========================
    // Order history
    // ==========================
    @GetMapping("/orders")
    public String orderHistory(
            HttpSession session,
            Model model) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders =
                orderService.getOrdersByUser(
                        user.getId()
                );

        model.addAttribute(
                "orders",
                orders
        );

        return "order-history";
    }

    // ==========================
    // View single order
    // ==========================
    @GetMapping("/orders/{id}")
    public String viewOrder(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        Order order =
                orderService.findOrderById(id);

        if (order == null) {
            return "redirect:/orders";
        }

        // Make sure user can only view
        // their own order
        if (order.getUser() == null ||
                !order.getUser()
                      .getId()
                      .equals(user.getId())) {

            return "redirect:/orders";
        }

        model.addAttribute(
                "order",
                order
        );

        return "order-details";
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