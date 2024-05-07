package com.uevitondev.deliverybackend.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public class CartItemDTO implements Serializable {
    @NotNull(message = "productId: is mandatory")
    private UUID productId;
    @NotNull(message = "quantity: is mandatory")
    @Min(value = 1, message = "qtd: minimum value = 1")
    private Integer quantity;
    private String observation;

    public CartItemDTO() {
    }

    public CartItemDTO(UUID productId, Integer quantity, String observation) {
        this.productId = productId;
        this.quantity = quantity;
        this.observation = observation;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
