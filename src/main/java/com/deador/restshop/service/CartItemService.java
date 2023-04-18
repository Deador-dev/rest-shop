package com.deador.restshop.service;

import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.dto.cartItemResponse.CartItemResponse;
import com.deador.restshop.entity.Cart;
import com.deador.restshop.entity.Smartphone;

import java.util.List;

public interface CartItemService {
    List<CartItemResponse> getCartItemResponsesByCartResponse(CartResponse cartResponse);

    CartItemResponse addCartItem(Cart cart, Smartphone smartphone);
}
