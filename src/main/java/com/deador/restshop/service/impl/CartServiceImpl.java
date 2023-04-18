package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.entity.Cart;
import com.deador.restshop.entity.User;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.factory.ObjectFactory;
import com.deador.restshop.repository.CartRepository;
import com.deador.restshop.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {
    private static final String USER_NOT_FOUND_FOR_CREATING_CART = "User not found for creating cart";
    private final CartRepository cartRepository;
    private final ObjectFactory objectFactory;
    private final DTOConverter dtoConverter;

    public CartServiceImpl(CartRepository cartRepository,
                           ObjectFactory objectFactory,
                           DTOConverter dtoConverter) {
        this.cartRepository = cartRepository;
        this.objectFactory = objectFactory;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public CartResponse getCartResponseByUserId(Long id) {
        return null;
    }

    @Override
    public CartResponse createCartForUser(User user) {
        if (user == null || user.getId() == null) {
            throw new NotExistException(USER_NOT_FOUND_FOR_CREATING_CART);
        }

        Cart cart = (Cart) objectFactory.createObject(Cart.class);
        cart.setQuantity(0);
        cart.setPrice(0.0);
        cart.setUser(user);

        return dtoConverter.convertToDTO(cartRepository.save(cart), CartResponse.class);
    }
}
