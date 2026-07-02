package com.ecom.app.repository;

import com.ecom.app.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Collection<Order> findByUserId(Long userId);
}
