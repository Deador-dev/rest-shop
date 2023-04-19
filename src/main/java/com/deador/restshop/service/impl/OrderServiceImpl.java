package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.order.OrderProfile;
import com.deador.restshop.dto.order.OrderResponse;
import com.deador.restshop.entity.Order;
import com.deador.restshop.repository.OrderRepository;
import com.deador.restshop.service.CartService;
import com.deador.restshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String DEFAULT_DELIVERY_STATUS = "Processing";
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final DTOConverter dtoConverter;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            CartService cartService,
                            DTOConverter dtoConverter) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public OrderResponse addOrder(OrderProfile orderProfile) {
        Order order = dtoConverter.convertToEntity(orderProfile, Order.class);
        order.setDeliveryStatus(DEFAULT_DELIVERY_STATUS);

        return null;
    }
}
