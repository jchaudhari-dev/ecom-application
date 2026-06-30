package com.ecom.app.service;

import com.ecom.app.dto.ProductRequest;
import com.ecom.app.dto.ProductResponse;
import com.ecom.app.model.Product;
import com.ecom.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest productRequest) {
        // Logic to add product to the database
        Product product = new Product();
        updateProduct(product,productRequest);
        Product response= productRepository.save(product);
        return mapToProduct(response);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(this::mapToProduct)
                .toList();
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest){
        return productRepository.findById(id)
                .map(product -> {
                    updateProduct(product, productRequest);
                    Product updatedProduct = productRepository.save(product);
                    return mapToProduct(updatedProduct);
                })
                .orElse(null);
    }

    private ProductResponse mapToProduct(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setCategory(product.getCategory());
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setId(product.getId());
        productResponse.setIsActive(product.getIsActive());
        return productResponse;
    }

    public void updateProduct(Product product, ProductRequest productRequest){
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setIsActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProduct(String keyword) {
        return productRepository.searchProductByName(keyword)
                .stream()
                .map(this::mapToProduct)
                .toList();
    }
}
