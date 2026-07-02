package com.ecom.app.service;

import com.ecom.app.dto.CartItemResponse;
import com.ecom.app.dto.OrderItemDTO;
import com.ecom.app.dto.OrderResponse;
import com.ecom.app.model.*;
import com.ecom.app.repository.CartItemRepository;
import com.ecom.app.repository.OrderRepository;
import com.ecom.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartItemService cartItemService;

    public Optional<OrderResponse> orderCreate(String userId) {

        List<CartItemResponse> cartItem =cartItemService.getCartItems(userId);
        if (cartItem.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();

        BigDecimal totalPrice = cartItem.stream()
                .map(CartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setStatus(OrderStatus.SUCCESSFUL);
        order.setUser(user);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItem.stream()
                .map(item ->
                        new OrderItem(null, item.getProductId(), item.getQuantity(), item.getTotalPrice(), order)).toList();
        order.setItem(orderItems);
        Order savedOrder = orderRepository.save(order);

        cartItemService.clearCart(user);
        return Optional.of(mapToOrderResponse(savedOrder));

    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getUser().getId(), order.getTotalAmount(), order.getStatus(), order.getItem().stream().map(item -> new OrderItemDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getQuantity(),
                item.getPrice()
        )).toList());

    }
}
