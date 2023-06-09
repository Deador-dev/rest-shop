package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.cartItemResponse.CartItemResponse;
import com.deador.restshop.model.Cart;
import com.deador.restshop.model.CartItem;
import com.deador.restshop.model.Smartphone;
import com.deador.restshop.exception.DatabaseRepositoryException;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.factory.ObjectFactory;
import com.deador.restshop.repository.CartItemRepository;
import com.deador.restshop.service.CartItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CartItemServiceImpl implements CartItemService {
    private static final String CART_ITEM_NOT_FOUND_BY_ID = "Cart item not found by id: %s";
    private static final String CART_ITEM_DELETING_ERROR = "Can't delete cart item cause of relationships";
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
    public List<CartItem> getCartItemsByCartId(Long id) {
        log.debug("get list of cart items by cart id '{}'", id);
        return cartItemRepository.findAllByCartId(id);
    }

    @Override
    public List<CartItemResponse> getCartItemResponsesByCartId(Long id) {
        log.debug("get list of cart item responses by cart id '{}'", id);
        return cartItemRepository.findAllByCartId(id).stream()
                .map(cartItem -> (CartItemResponse) dtoConverter.convertToDTO(cartItem, CartItemResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public CartItem getCartItemById(Long id) {
        log.debug("get cart item by id '{}'", id);
        return cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("cart item not found by id '{}'", id);
                    return new NotExistException(String.format(CART_ITEM_NOT_FOUND_BY_ID, id));
                });
    }

    @Override
    public CartItemResponse addCartItem(Cart cart, Smartphone smartphone) {
        CartItem cartItem = (CartItem) objectFactory.createObject(CartItem.class);

        cartItem.setCart(cart);
        cartItem.setSmartphone(smartphone);
        cartItemRepository.save(cartItem);

        log.debug("adding smartphone by id '{}' to cart with id '{}'", smartphone.getId(), cart.getId());
        return dtoConverter.convertToDTO(cartItemRepository.save(cartItem), CartItemResponse.class);
    }

    @Override
    public CartItemResponse deleteCartItemById(Long id) {
        CartItem cartItem = getCartItemById(id);

        try {
            cartItemRepository.deleteById(id);
            cartItemRepository.flush();
        } catch (DataAccessException | ValidationException exception) {
            throw new DatabaseRepositoryException(CART_ITEM_DELETING_ERROR);
        }

        log.debug("cart item by id '{}' was successfully deleted", cartItem.getId());
        return dtoConverter.convertToDTO(cartItem, CartItemResponse.class);
    }
}
