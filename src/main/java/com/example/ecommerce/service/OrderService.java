package com.example.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Save order
    public Order saveOrder(Order order) {

        return orderRepository.save(order);
    }

    // Get all orders
    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    // Find order by ID
    public Order findOrderById(Long id) {

        return orderRepository.findById(id)
                .orElse(null);
    }

    // Get orders of a particular user
    public List<Order> getOrdersByUser(Long userId) {

        return orderRepository
                .findByUserIdOrderByOrderDateDesc(userId);
    }

    // Get orders by status
    public List<Order> getOrdersByStatus(String status) {

        return orderRepository.findByStatus(status);
    }

    // Update order
    public Order updateOrder(Order order) {

        return orderRepository.save(order);
    }

    // Delete order
    public void deleteOrder(Long id) {

        orderRepository.deleteById(id);
    }
}