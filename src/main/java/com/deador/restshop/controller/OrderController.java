package com.deador.restshop.controller;

import com.deador.restshop.dto.order.OrderProfile;
import com.deador.restshop.dto.order.OrderResponse;
import com.deador.restshop.security.UserPrincipal;
import com.deador.restshop.service.OrderItemService;
import com.deador.restshop.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
//@RequestMapping("/shop")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderController(OrderService orderService,
                           OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and authentication.principal.id == #orderProfile.userId)")
    @PostMapping("/shop/order")
    public ResponseEntity<OrderResponse> addOrder(@Valid @RequestBody OrderProfile orderProfile,
                                                  @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(user.getId(), orderProfile));
    }
}
