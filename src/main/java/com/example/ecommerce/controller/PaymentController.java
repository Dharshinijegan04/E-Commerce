package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PaymentController {

    // Payment page
    @GetMapping("/payment")
    public String paymentPage(
            @RequestParam double totalAmount,
            Model model) {

        model.addAttribute(
                "totalAmount",
                totalAmount
        );

        return "payment";
    }

    // Cash on Delivery
    @PostMapping("/payment/cod")
    public String cashOnDelivery() {

        return "redirect:/checkout";
    }

    // Order success page
    @GetMapping("/order-success")
    public String successPage() {

        return "order-success";
    }
}