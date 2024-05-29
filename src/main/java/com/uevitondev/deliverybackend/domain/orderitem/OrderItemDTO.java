package com.uevitondev.deliverybackend.domain.orderitem;

import java.io.Serializable;
import java.util.UUID;

public class OrderItemDTO implements Serializable {
    private UUID id;
    private Integer quantity;
    private Double total;
    private String observation;

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
        this.total = orderItem.getTotal();
        this.observation = orderItem.getObservation();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
