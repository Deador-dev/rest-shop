package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.dto.cartItemResponse.CartItemResponse;
import com.deador.restshop.entity.Cart;
import com.deador.restshop.entity.CartItem;
import com.deador.restshop.entity.Smartphone;
import com.deador.restshop.factory.ObjectFactory;
import com.deador.restshop.repository.CartItemRepository;
import com.deador.restshop.service.CartItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final DTOConverter dtoConverter;
    private final ObjectFactory objectFactory;

    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               DTOConverter dtoConverter,
                               ObjectFactory objectFactory) {
        this.cartItemRepository = cartItemRepository;
        this.dtoConverter = dtoConverter;
        this.objectFactory = objectFactory;
    }

    @Override
    public List<CartItemResponse> getCartItemResponsesByCartResponse(CartResponse cartResponse) {
        return cartItemRepository.findAllByCartId(cartResponse.getId()).stream()
                .map(cartItem -> (CartItemResponse) dtoConverter.convertToDTO(cartItem, CartItemResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemResponse addCartItem(Cart cart, Smartphone smartphone) {
        CartItem cartItem = (CartItem) objectFactory.createObject(CartItem.class);

        cartItem.setCart(cart);
        cartItem.setSmartphone(smartphone);
        cartItemRepository.save(cartItem);

        log.debug("adding smartphone by id {} to cart", smartphone.getId());
        return dtoConverter.convertToDTO(cartItemRepository.save(cartItem), CartItemResponse.class);
    }
}
