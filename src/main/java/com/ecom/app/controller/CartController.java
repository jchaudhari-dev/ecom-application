package com.ecom.app.controller;

import com.ecom.app.dto.CartItemRequest;
import com.ecom.app.dto.CartItemResponse;
import com.ecom.app.model.CartItem;
import com.ecom.app.service.CartItemService;
import com.ecom.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartItemService cartItemService;
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID")  String userId,
            @RequestBody CartItemRequest cartItemRequest
    ) {
        // Logic to add item to cart would go here
        if(!cartItemService.addItemToCart(userId, cartItemRequest)){
            return ResponseEntity.badRequest().body("Failed to add item to cart. Check product availability or user ID.");
        }
        return ResponseEntity.ok("Item added to cart successfully");
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeCart(
            @RequestHeader("X-User-ID")  String userId,
            @PathVariable Long productId
    ){
        boolean deleted = cartItemService.deleteFromCart(userId,productId);
        if(deleted){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get-cart")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@RequestHeader("X-User-ID") String userId) {
        List<CartItemResponse> cartItemResponse = cartItemService.getCartItems(userId);
        if(cartItemResponse == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartItemResponse);
    }

    @GetMapping()
    public ResponseEntity<List<CartItem>> getCartItemsWithCOmpleteDetails(@RequestHeader("X-User-ID") String userId) {
        List<CartItem> cartItemResponse = cartItemService.getCartItemsDetails(userId);
        if(cartItemResponse == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartItemResponse);
    }


}
