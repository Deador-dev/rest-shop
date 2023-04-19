package com.deador.restshop.service;

import com.deador.restshop.dto.cartItemResponse.CartItemResponse;
import com.deador.restshop.dto.orderItemResponse.OrderItemResponse;
import com.deador.restshop.entity.CartItem;
import com.deador.restshop.entity.Order;

import java.util.List;

public interface OrderItemService {
    List<OrderItemResponse> createOrderItemsForOrder(List<CartItem> cartItems, Order order);

    List<OrderItemResponse> getOrderItemResponsesByOrderId(Long id);
}
