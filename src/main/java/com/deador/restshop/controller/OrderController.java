package com.deador.restshop.controller;

import com.deador.restshop.dto.order.OrderProfile;
import com.deador.restshop.dto.order.OrderResponse;
import com.deador.restshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> addOrder(@Valid @RequestBody OrderProfile orderProfile){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(orderProfile));
    }
}
