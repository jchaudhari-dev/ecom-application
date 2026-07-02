package com.ecom.app.dto;

import com.ecom.app.model.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> items;
    private LocalDateTime createdAt;

    public OrderResponse(Long id, Long id1, BigDecimal totalAmount, OrderStatus status, List<OrderItemDTO> list) {
    }
}
