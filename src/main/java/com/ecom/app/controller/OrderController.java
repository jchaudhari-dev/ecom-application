package com.ecom.app.controller;

import com.ecom.app.dto.OrderResponse;
import com.ecom.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Optional<OrderResponse>> createOrder(@RequestHeader("X-User-ID") String userId) {
        Optional<OrderResponse> response= orderService.orderCreate(userId);
        return response.isPresent()?ResponseEntity.ok(response):ResponseEntity.badRequest().build();
    }
}
