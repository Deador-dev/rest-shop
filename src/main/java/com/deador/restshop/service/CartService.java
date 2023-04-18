package com.deador.restshop.service;

import com.deador.restshop.dto.cart.CartResponse;

public interface CartService {
    CartResponse getCartResponseByUserId(Long id);
}
