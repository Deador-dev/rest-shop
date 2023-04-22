package com.deador.restshop.service;

import com.deador.restshop.dto.orderItemResponse.OrderItemResponse;
import com.deador.restshop.model.CartItem;
import com.deador.restshop.model.Order;

import java.util.List;

public interface OrderItemService {
    List<OrderItemResponse> createOrderItemsForOrder(List<CartItem> cartItems, Order order);

    List<OrderItemResponse> getOrderItemResponsesByOrderId(Long id);
}
