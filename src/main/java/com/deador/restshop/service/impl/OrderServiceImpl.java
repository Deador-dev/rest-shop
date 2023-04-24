package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.order.OrderProfile;
import com.deador.restshop.dto.order.OrderResponse;
import com.deador.restshop.model.Cart;
import com.deador.restshop.model.CartItem;
import com.deador.restshop.model.Order;
import com.deador.restshop.exception.AlreadyExistException;
import com.deador.restshop.exception.BadRequestException;
import com.deador.restshop.exception.EmptyCartException;
import com.deador.restshop.exception.NotExistException;
import com.deador.restshop.repository.OrderRepository;
import com.deador.restshop.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
    private static final String ORDER_NOT_FOUND_BY_ID = "Order not found by id: %s";
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
    public List<OrderResponse> getAllOrderResponses() {
        List<OrderResponse> orderResponses = orderRepository.findAll().stream()
                .map(order -> (OrderResponse) dtoConverter.convertToDTO(order, OrderResponse.class))
                .collect(Collectors.toList());

        log.debug("get list of order responses '{}'", orderResponses);
        return orderResponses;
    }

    @Override
    public Order getOrderById(Long id) {
        log.debug("get order by id '{}'", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("order not found by id '{}'", id);
                    return new NotExistException(String.format(ORDER_NOT_FOUND_BY_ID, id));
                });
    }

    @Override
    public OrderResponse getOrderResponseById(Long id) {
        log.debug("get order response by id '{}'", id);
        return dtoConverter.convertToDTO(getOrderById(id), OrderResponse.class);
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

        cartService.clearCartByCartId(cart.getId());

        log.debug("order was successfully created '{}'", order);
        return orderResponse;
    }

    @Override
    public OrderResponse updateDeliveryStatusById(Long id, String deliveryStatus) {
        Order order = getOrderById(id);

        if (deliveryStatus.isEmpty()) {
            throw new BadRequestException("Delivery status can't be empty");
        } else if (order.getDeliveryStatus().equals(deliveryStatus)) {
            throw new AlreadyExistException(String.format("Delivery status '%s' already exist", deliveryStatus));
        }

        order.setDeliveryStatus(deliveryStatus);
        order = orderRepository.save(order);

        log.debug("the delivery status of the order with id '{}' was successfully updated to '{}'", id, deliveryStatus);
        return dtoConverter.convertToDTO(order, OrderResponse.class);
    }
}
