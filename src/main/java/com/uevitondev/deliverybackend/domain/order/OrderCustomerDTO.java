package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.address.AddressDTO;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItemDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderCustomerDTO(
        UUID id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime closedAt,
        OrderStatus status,
        OrderPayment paymentMethod,
        Double total,
        OrderCustomerDataDTO customerData,
        OrderStoreDataDTO storeData,
        AddressDTO deliveryAddress,
        List<OrderItemDTO> orderItems

) {


}
