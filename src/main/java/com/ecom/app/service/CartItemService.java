package com.ecom.app.service;

import com.ecom.app.dto.CartItemRequest;
import com.ecom.app.dto.CartItemResponse;
import com.ecom.app.model.CartItem;
import com.ecom.app.model.Product;
import com.ecom.app.model.User;
import com.ecom.app.repository.CartItemRepository;
import com.ecom.app.repository.ProductRepository;
import com.ecom.app.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public boolean addItemToCart(String userId, CartItemRequest cartItemRequest) {

        Optional<Product> productOpt = productRepository.findById(cartItemRequest.getProductId());
        if (productOpt.isEmpty()) {
            return false;
        }

        Product product = productOpt.get();
        if (product.getStockQuantity() < cartItemRequest.getQuantity()) {
            return false; // Not enough stock
        }

        Optional<User> userOpt = userRepository.findById(Long.parseLong(userId));
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
            // Update existing cart item
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            existingCartItem.setTotalPrice(product.getPrice().multiply(java.math.BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        } else {
            // Create new cart item
            CartItem newCartItem = new CartItem();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(cartItemRequest.getQuantity());
            newCartItem.setTotalPrice(product.getPrice().multiply(java.math.BigDecimal.valueOf(cartItemRequest.getQuantity())));
            cartItemRepository.save(newCartItem);
        }
        return true;
    }

    public boolean deleteFromCart(String userId, Long productId) {

        Optional<Product> productOpt = productRepository.findById(productId);

        Optional<User> userOpt = userRepository.findById(Long.parseLong(userId));

        if (productOpt.isPresent() && userOpt.isPresent()) {
            cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            return true;
        }
        return false;
    }

    public List<CartItemResponse> getCartItems(String userId) {
        Optional<User> userOpt = userRepository.findById(Long.parseLong(userId));
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        List<CartItem> cartItems = cartItemRepository.findAll().stream()
                .filter(cartItem -> cartItem.getUser().equals(user))
                .toList();

        return cartItems.stream()
                .map(cartItem -> {
                    CartItemResponse response = new CartItemResponse();
                    response.setProductId(cartItem.getProduct().getId());
                    response.setProductName(cartItem.getProduct().getName());
                    response.setQuantity(cartItem.getQuantity());
                    response.setTotalPrice(cartItem.getTotalPrice());
                    return response;
                })
                .toList();
    }

    public List<CartItem> getCartItemsDetails(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .map(cartItemRepository::findByUser)
                .orElseGet(List::of);
    }
}
