package com.deador.restshop.service;

import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.entity.User;

public interface CartService {
    CartResponse getCartResponseByUserId(Long id);

    CartResponse createCartForUser(User user);
}
