package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.customer.Customer;
import com.uevitondev.deliverybackend.domain.orderitem.ShoppingCartDTO;
import com.uevitondev.deliverybackend.domain.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
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

    @GetMapping("/customer")
    public ResponseEntity<List<OrderDTO>> findOrdersByCustomer() {
        Customer customer = (Customer) UserService.getUserAuthenticated();
        return ResponseEntity.ok().body(orderService.findAllOrdersByCustomer(customer));
    }

    @GetMapping("/{id}/customer")
    public ResponseEntity<OrderCustomerDTO> findOrderByIdWithOrderItems(@PathVariable UUID id) {
        return ResponseEntity.ok().body(orderService.findOrderByIdWithOrderItems(id));
    }


    @PostMapping
    public ResponseEntity<OrderDTO> saveNewOrder(@RequestBody @Valid ShoppingCartDTO dto) {
        return ResponseEntity.ok().body(orderService.saveNewOrder(dto));
    }
}
