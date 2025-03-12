package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.orderitem.ShoppingCartDTO;
import com.uevitondev.deliverybackend.domain.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;
    private final SimpMessagingTemplate messagingTemplate;

    public OrderController(UserService userService, OrderService orderService, SimpMessagingTemplate messagingTemplate) {
        this.userService = userService;
        this.orderService = orderService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAllOrders() {
        return ResponseEntity.ok().body(orderService.findAllOrders());
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<List<OrderDTO>> findAllOrdersByStore(@PathVariable(name = "id") UUID storeId) {
        return ResponseEntity.ok().body(orderService.findAllOrdersByStore(storeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(orderService.findOrderById(id));
    }

    @GetMapping("/customer")
    public ResponseEntity<List<OrderDTO>> findOrdersByCustomer() {
        Customer customer = (Customer) userService.getUserAuthenticated();
        return ResponseEntity.ok().body(orderService.findAllOrdersByCustomer(customer));
    }

    @GetMapping("/{id}/customer")
    public ResponseEntity<OrderDetailsDTO> findOrderByIdWithOrderItems(@PathVariable UUID id) {
        return ResponseEntity.ok().body(orderService.findOrderByIdWithOrderItems(id));
    }


    @PostMapping
    public ResponseEntity<OrderDTO> saveNewOrder(@RequestBody @Valid ShoppingCartDTO dto) {
        var orderDto = orderService.saveNewOrder(dto);
        this.messagingTemplate.convertAndSend("/topic/orders", orderDto);
        return ResponseEntity.ok().body(orderDto);
    }
}
