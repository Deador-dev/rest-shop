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

    // FIXME: 20.04.2023 maybe need to change @RequestParam Long id TO @AuthenticationPrincipal UserPrincipal user -> user.getId() ?
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == #id)")
    @GetMapping("/cart")
    public ResponseEntity<CartResponse> getCart(@RequestParam Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCartResponseByUserId(id));
    }

    // FIXME: 20.04.2023 When an admin adds a smartphone from the user's cart, they do not change the user's cart, but their own personal cart.
    //  This is because the @AuthenticationPrincipal UserPrincipal user annotation refers to the currently logged-in admin, not the user whose cart is being modified.
    //  To modify the user's cart may need to use a different method, such as passing the user's ID as a @RequestParam.
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == @cartServiceImpl.getCartByUserId(#user.id).user.id)")
    @PostMapping("/cart/add-to-cart")
    public ResponseEntity<CartResponse> addSmartphoneToCart(@RequestParam Long smartphoneId,
                                                            @RequestParam Integer quantity,
                                                            @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addSmartphoneToCart(user.getId(), smartphoneId, quantity));
    }

    // FIXME: 20.04.2023 When an admin deletes a smartphone from the user's cart, they do not change the user's cart, but their own personal cart.
    //  This is because the @AuthenticationPrincipal UserPrincipal user annotation refers to the currently logged-in admin, not the user whose cart is being modified.
    //  To modify the user's cart may need to use a different method, such as passing the user's ID as a @RequestParam.
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and authentication.principal.id == @cartItemServiceImpl.getCartItemById(#cartItemId).cart.user.id)")
    @DeleteMapping("/cart/delete-from-cart/{id}")
    public ResponseEntity<CartResponse> deleteSmartphoneFromCart(@PathVariable(name = "id") Long cartItemId,
                                                                 @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.OK).body(cartService.deleteSmartphoneFromCart(user.getId(), cartItemId));
    }
}