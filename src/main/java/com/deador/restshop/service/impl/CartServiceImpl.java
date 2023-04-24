package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.cart.CartResponse;
import com.deador.restshop.dto.user.UserResponse;
import com.deador.restshop.model.Cart;
import com.deador.restshop.model.Smartphone;
import com.deador.restshop.model.User;
import com.deador.restshop.exception.DatabaseRepositoryException;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.factory.ObjectFactory;
import com.deador.restshop.repository.CartItemRepository;
import com.deador.restshop.repository.CartRepository;
import com.deador.restshop.repository.UserRepository;
import com.deador.restshop.service.CartItemService;
import com.deador.restshop.service.CartService;
import com.deador.restshop.service.SmartphoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;

@Service
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {
    private static final String USER_NOT_FOUND_FOR_CREATING_CART = "User not found for creating cart";
    private static final String CART_NOT_FOUND_BY_ID = "Cart not found by id: %s";
    private static final String CART_NOT_FOUND_BY_USER_ID = "Cart not found by user id: %s";
    private static final String USER_NOT_FOUND_BY_ID = "User not found by id: %s";
    private static final String CART_CLEARING_ERROR = "Can't clear the cart cause of relationships";
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final SmartphoneService smartphoneService;
    private final CartItemService cartItemService;
    private final ObjectFactory objectFactory;
    private final DTOConverter dtoConverter;

    public CartServiceImpl(UserRepository userRepository,
                           CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           SmartphoneService smartphoneService,
                           CartItemService cartItemService,
                           ObjectFactory objectFactory,
                           DTOConverter dtoConverter) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.smartphoneService = smartphoneService;
        this.cartItemService = cartItemService;
        this.objectFactory = objectFactory;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public Cart getCartByUserId(Long id) {
        log.debug("get cart by user id '{}'", id);
        return cartRepository.findByUserId(id)
                .orElseThrow(() -> {
                    log.error("cart not found by user id '{}'", id);
                    return new NotExistException(String.format(CART_NOT_FOUND_BY_USER_ID, id));
                });
    }

    @Override
    public Cart getCartByCartId(Long id) {
        log.debug("get cart by id '{}'", id);
        return cartRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("cart not found by id '{}'", id);
                    return new NotExistException(String.format(CART_NOT_FOUND_BY_ID, id));
                });
    }

    @Override
    public CartResponse getCartResponseByUserId(Long id) {
        CartResponse cartResponse = dtoConverter.convertToDTO(getCartByUserId(id), CartResponse.class);

        cartResponse.setUser(dtoConverter.convertToDTO(userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("user not found by id '{}'", id);
                    return new NotExistException(String.format(USER_NOT_FOUND_BY_ID, id));
                }), UserResponse.class));

        cartResponse.setCartItems(cartItemService.getCartItemResponsesByCartId(cartResponse.getId()));

        log.debug("get cart response '{}' by user id '{}'", cartResponse, id);
        return cartResponse;
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

        log.debug("cart was created successfully for user with email '{}'", user.getEmail());
        return dtoConverter.convertToDTO(cartRepository.save(cart), CartResponse.class);
    }

    @Override
    public CartResponse addSmartphoneToCart(Long userId, Long smartphoneId, Integer quantity) {
        Cart cart = getCartByUserId(userId);
        Smartphone smartphone = smartphoneService.getSmartphoneById(smartphoneId);
        Double cartPrice = 0.0;

        for (int i = 0; i < quantity; i++) {
            cartItemService.addCartItem(cart, smartphone);
            cartPrice += smartphone.getPrice();
        }
        cart.setQuantity(cart.getQuantity() + quantity);
        cart.setPrice(cart.getPrice() + cartPrice);

        CartResponse cartResponse = dtoConverter.convertToDTO(cartRepository.save(cart), CartResponse.class);
        cartResponse.setCartItems(cartItemService.getCartItemResponsesByCartId(cartResponse.getId()));

        log.debug("smartphone with id '{}' was added to cart successfully for user with id '{}'", smartphoneId, userId);
        return cartResponse;
    }

    @Override
    public CartResponse deleteSmartphoneFromCart(Long userId, Long cartItemId) {
        Cart cart = getCartByUserId(userId);

        cart.setQuantity(cart.getQuantity() - 1);
        cart.setPrice(cart.getPrice() - cartItemService.getCartItemById(cartItemId).getSmartphone().getPrice());

        cartItemService.deleteCartItemById(cartItemId);

        CartResponse cartResponse = dtoConverter.convertToDTO(cartRepository.save(cart), CartResponse.class);
        cartResponse.setCartItems(cartItemService.getCartItemResponsesByCartId(cartResponse.getId()));

        log.debug("cart item with id '{}' was deleted from cart successfully for user with id '{}'", cartItemId, userId);
        return cartResponse;
    }

    @Override
    public CartResponse clearCartByCartId(Long id) {
        Cart cart = getCartByCartId(id);

        try {
            cartItemService.getCartItemsByCartId(id)
                    .forEach(cartItem -> cartItemService.deleteCartItemById(cartItem.getId()));

            cart.setQuantity(0);
            cart.setPrice(0.0);
            cart = cartRepository.save(cart);
        } catch (DataAccessException | ValidationException exception) {
            throw new DatabaseRepositoryException(CART_CLEARING_ERROR);
        }

        log.debug("cart with id '{}' was successfully cleared", id);
        return dtoConverter.convertToDTO(cart, CartResponse.class);
    }
}
