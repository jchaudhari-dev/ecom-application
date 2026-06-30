package com.ecom.app.repository;

import com.ecom.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActiveTrue();

    @Query("select p from Product p where p.isActive = true and p.stockQuantity>0 and lower(p.name) like lower(concat('%', :name, '%'))")
    List<Product> searchProductByName(String name);
}
