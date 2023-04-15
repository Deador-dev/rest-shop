package com.deador.restshop.service.impl;

import com.deador.restshop.dto.order.OrderProfile;
import com.deador.restshop.dto.order.OrderResponse;
import com.deador.restshop.repository.OrderRepository;
import com.deador.restshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponse addOrder(OrderProfile orderProfile) {
        return null;
    }
}
