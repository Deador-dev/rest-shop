package com.deador.restshop.service;

import com.deador.restshop.dto.order.OrderProfile;
import com.deador.restshop.dto.order.OrderResponse;

public interface OrderService {
    OrderResponse addOrder(OrderProfile orderProfile);
}
