package com.uevitondev.deliverybackend.domain.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShoppingCartDTO implements Serializable {

    @NotNull(message = "pizzeriaId: is mandatory")
    private UUID storeId;
    @NotNull(message = "addressId: is mandatory")
    private UUID addressId;

    @NotEmpty(message = "cartItems: cannot be empty")
    private final Set<@Valid CartItemDTO> cartItems = new HashSet<>();

    public ShoppingCartDTO() {
    }

    public ShoppingCartDTO(UUID storeId, UUID addressId) {
        this.storeId = storeId;
        this.addressId = addressId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public void setAddressId(UUID addressId) {
        this.addressId = addressId;
    }

    public Set<CartItemDTO> getCartItems() {
        return cartItems;
    }
}
