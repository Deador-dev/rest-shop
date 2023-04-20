package com.deador.restshop.service;

import com.deador.restshop.dto.order.OrderProfile;
import com.deador.restshop.dto.order.OrderResponse;
import com.deador.restshop.entity.Order;

import java.util.List;

public interface OrderService {


    List<OrderResponse> getAllOrderResponses();

    Order getOrderById(Long id);

    OrderResponse getOrderResponseById(Long id);

    OrderResponse addOrder(Long userId, OrderProfile orderProfile);

    OrderResponse updateDeliveryStatusById(Long id, String deliveryStatus);
}
