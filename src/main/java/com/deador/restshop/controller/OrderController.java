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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrderResponses());
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and authentication.principal.id == @orderServiceImpl.getOrderById(#id).user.id)")
    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderResponseById(id));
    }

    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and authentication.principal.id == #orderProfile.userId)")
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> addOrder(@Valid @RequestBody OrderProfile orderProfile,
                                                  @AuthenticationPrincipal UserPrincipal user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addOrder(user.getId(), orderProfile));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/order/{id}")
    public ResponseEntity<OrderResponse> updateDeliveryStatusOfOrder(@PathVariable Long id,
                                                                     @RequestParam() String deliveryStatus) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.updateDeliveryStatusById(id, deliveryStatus));
    }
}