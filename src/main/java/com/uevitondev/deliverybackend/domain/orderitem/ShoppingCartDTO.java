package com.uevitondev.deliverybackend.domain.orderitem;

import com.uevitondev.deliverybackend.domain.address.AddressDTO;
import com.uevitondev.deliverybackend.domain.store.StoreDTO;

import java.util.Set;

public record ShoppingCartDTO(
        AddressDTO address,
        StoreDTO store,
        String paymentMethod,
        String note,
        Set<CartItemDTO> cartItems
) {
}




