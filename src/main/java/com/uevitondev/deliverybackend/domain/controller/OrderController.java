package com.uevitondev.deliverybackend.domain.controller;

import com.uevitondev.deliverybackend.domain.dto.OrderDTO;
import com.uevitondev.deliverybackend.domain.dto.ShoppingCartDTO;
import com.uevitondev.deliverybackend.domain.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/delivery/v1/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAllOrders() {
        return ResponseEntity.ok().body(orderService.findAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(orderService.findOrderById(id));
    }

    @PostMapping
    public ResponseEntity<OrderDTO> saveNewOrder(@RequestBody @Valid ShoppingCartDTO dto) {
        return ResponseEntity.ok().body(orderService.saveNewOrder(dto));
    }
}
