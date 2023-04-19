package com.deador.restshop.service;

import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.dto.cartItemResponse.CartItemResponse;
import com.deador.restshop.entity.Cart;
import com.deador.restshop.entity.CartItem;
import com.deador.restshop.entity.Smartphone;
import com.deador.restshop.security.UserPrincipal;

import java.util.List;

public interface CartItemService {
    List<CartItem> getCartItemsByCartId(Long id);

    List<CartItemResponse> getCartItemResponsesByCartId(Long id);

    CartItem getCartItemById(Long id);

    CartItemResponse addCartItem(Cart cart, Smartphone smartphone);

    CartItemResponse deleteCartItemById(Long id);

}
