package com.deador.restshop.service;

import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.model.Cart;
import com.deador.restshop.model.User;

public interface CartService {
    Cart getCartByUserId(Long id);

    Cart getCartByCartId(Long id);

    CartResponse getCartResponseByUserId(Long id);

    CartResponse createCartForUser(User user);

    CartResponse addSmartphoneToCart(Long userId, Long smartphoneId, Integer quantity);

    CartResponse deleteSmartphoneFromCart(Long userId, Long cartItemId);

    CartResponse clearCartByCartId(Long id);
}
