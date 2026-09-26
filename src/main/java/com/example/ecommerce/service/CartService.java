package com.example.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    // Get user's cart
    public Cart getCart(User user) {

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {

                    Cart cart = new Cart(user);

                    return cartRepository.save(cart);
                });
    }

    // Add product to cart
    public Cart addToCart(User user, Long productId, int quantity) {

        Cart cart = getCart(user);

        Product product = productRepository
                .findById(productId)
                .orElse(null);

        if (product == null) {
            return cart;
        }

        if (quantity <= 0) {
            quantity = 1;
        }

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        productId
                )
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + quantity
            );

            cartItemRepository.save(cartItem);

        } else {

            cartItem = new CartItem(product, quantity);

            cart.addItem(cartItem);

            cartRepository.save(cart);
        }

        return cart;
    }

    // Remove product from cart
    public void removeFromCart(User user, Long productId) {

        Cart cart = getCart(user);

        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductId(
                        cart.getId(),
                        productId
                )
                .orElse(null);

        if (cartItem != null) {

            cart.removeItem(cartItem);

            cartItemRepository.delete(cartItem);
        }
    }

    // Clear cart
    public void clearCart(User user) {

        Cart cart = getCart(user);

        cart.getItems().clear();

        cartRepository.save(cart);
    }
}