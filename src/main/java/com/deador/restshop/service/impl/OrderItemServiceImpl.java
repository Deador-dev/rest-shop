package com.deador.restshop.service.impl;

import com.deador.restshop.converter.DTOConverter;
import com.deador.restshop.dto.orderItemResponse.OrderItemResponse;
import com.deador.restshop.model.CartItem;
import com.deador.restshop.model.Order;
import com.deador.restshop.model.OrderItem;
import com.deador.restshop.factory.ObjectFactory;
import com.deador.restshop.repository.OrderItemRepository;
import com.deador.restshop.service.OrderItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final DTOConverter dtoConverter;
    private final ObjectFactory objectFactory;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                DTOConverter dtoConverter,
                                ObjectFactory objectFactory) {
        this.orderItemRepository = orderItemRepository;
        this.dtoConverter = dtoConverter;
        this.objectFactory = objectFactory;
    }

    @Override
    public List<OrderItemResponse> createOrderItemsForOrder(List<CartItem> cartItems, Order order) {
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = (OrderItem) objectFactory.createObject(OrderItem.class);
                    orderItem.setOrder(order);
                    orderItem.setSmartphone(cartItem.getSmartphone());
                    return orderItemRepository.save(orderItem);
                })
                .collect(Collectors.toList());

        log.debug("order items '{}' was successfully created to order with id '{}'", orderItems, order.getId());
        return orderItems.stream()
                .map(orderItem -> (OrderItemResponse) dtoConverter.convertToDTO(orderItem, OrderItemResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderItemResponse> getOrderItemResponsesByOrderId(Long id) {
        log.debug("get list of order item responses by order id '{}'", id);
        return orderItemRepository.findAllByOrderId(id).stream()
                .map(orderItem -> (OrderItemResponse) dtoConverter.convertToDTO(orderItem, OrderItemResponse.class))
                .collect(Collectors.toList());
    }
}
