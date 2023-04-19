package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.order.OrderProfile;
import com.deador.restshop.dto.order.OrderResponse;
import com.deador.restshop.entity.Cart;
import com.deador.restshop.entity.CartItem;
import com.deador.restshop.entity.Order;
import com.deador.restshop.exception.EmptyCartException;
import com.deador.restshop.repository.OrderRepository;
import com.deador.restshop.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
    private static final String EMPTY_CART_EXCEPTION = "Can't create order with 0 items in a cart";
    private static final String DELIVERY_STATUS_PROCESSING = "Processing";
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderItemService orderItemService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final DTOConverter dtoConverter;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserService userService,
                            OrderItemService orderItemService,
                            CartService cartService,
                            CartItemService cartItemService,
                            DTOConverter dtoConverter) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public OrderResponse addOrder(Long userId, OrderProfile orderProfile) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = dtoConverter.convertToEntity(orderProfile, Order.class);
        List<CartItem> cartItems = cartItemService.getCartItemsByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new EmptyCartException(EMPTY_CART_EXCEPTION);
        }

        order.setDeliveryStatus(DELIVERY_STATUS_PROCESSING);
        order.setTotalAmount(cart.getPrice());

        order = orderRepository.save(order);

        orderItemService.createOrderItemsForOrder(cartItems, order);

        OrderResponse orderResponse = dtoConverter.convertToDTO(order, OrderResponse.class);
        orderResponse.setUser(userService.getUserResponseById(userId));
        orderResponse.setOrderItems(orderItemService.getOrderItemResponsesByOrderId(orderResponse.getId()));

        // TODO: 19.04.2023 need to delete all cartItems from the cart when was created an order

        log.debug("order was successfully created {}", order);
        return orderResponse;
    }
}
