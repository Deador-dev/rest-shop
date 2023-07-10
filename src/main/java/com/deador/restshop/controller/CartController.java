package com.deador.restshop.controller;

import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.security.UserPrincipal;
import com.deador.restshop.service.CartItemService;
import com.deador.restshop.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;

    @Autowired
    public CartController(CartService cartService,
                          CartItemService cartItemService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }

    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == #userId)")
    @GetMapping("/cart")
    public ResponseEntity<CartResponse> getCart(@RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartResponseByUserId(userId));
    }

    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == @cartServiceImpl.getCartByUserId(#userId).user.id)")
    @PostMapping("/cart/add-to-cart")
    public ResponseEntity<CartResponse> addSmartphoneToCart(@RequestParam Long userId,
                                                            @RequestParam Long smartphoneId,
                                                            @RequestParam Integer quantity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addSmartphoneToCart(userId, smartphoneId, quantity));
    }

    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == @cartItemServiceImpl.getCartItemById(#cartItemId).cart.user.id)")
    @DeleteMapping("/cart/delete-from-cart/{id}")
    public ResponseEntity<CartResponse> deleteSmartphoneFromCart(@RequestParam Long userId,
                                                                 @PathVariable(name = "id") Long cartItemId) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.deleteSmartphoneFromCart(userId, cartItemId));
    }
}