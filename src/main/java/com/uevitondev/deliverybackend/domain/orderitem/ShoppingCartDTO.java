package com.uevitondev.deliverybackend.domain.orderitem;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShoppingCartDTO implements Serializable {

    @NotNull(message = "storeId: is mandatory")
    private UUID storeId;
    @NotNull(message = "userAddressId: is mandatory")
    private UUID userAddressId;
    private String paymentMethod;
    @NotEmpty(message = "cartItems: cannot be empty")
    private final Set<@Valid CartItemDTO> cartItems = new HashSet<>();

    public ShoppingCartDTO() {
    }

    public ShoppingCartDTO(UUID storeId, UUID userAddressId, String paymentMethod) {
        this.storeId = storeId;
        this.userAddressId = userAddressId;
        this.paymentMethod = paymentMethod;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public UUID getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(UUID userAddressId) {
        this.userAddressId = userAddressId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Set<CartItemDTO> getCartItems() {
        return cartItems;
    }
}
