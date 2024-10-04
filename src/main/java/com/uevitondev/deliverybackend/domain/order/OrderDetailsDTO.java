package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.address.AddressDTO;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItemDTO;
import com.uevitondev.deliverybackend.domain.store.StoreDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailsDTO(
        UUID id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime closedAt,
        OrderStatus status,
        OrderPayment paymentMethod,
        Double total,
        OrderCustomerDTO customer,
        StoreDTO store,
        AddressDTO deliveryAddress,
        List<OrderItemDTO> orderItems

) {


}
