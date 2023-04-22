package com.deador.restshop.service;

import com.deador.restshop.dto.cartItemResponse.CartItemResponse;
import com.deador.restshop.model.Cart;
import com.deador.restshop.model.CartItem;
import com.deador.restshop.model.Smartphone;

import java.util.List;

public interface CartItemService {
    List<CartItem> getCartItemsByCartId(Long id);

    List<CartItemResponse> getCartItemResponsesByCartId(Long id);

    CartItem getCartItemById(Long id);

    CartItemResponse addCartItem(Cart cart, Smartphone smartphone);

    CartItemResponse deleteCartItemById(Long id);

}
