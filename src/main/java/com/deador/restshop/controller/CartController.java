package com.deador.restshop.controller;

import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
@Slf4j
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public ResponseEntity<CartResponse> getCart(@RequestParam Long id){
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartResponseByUserId(id));
    }
}