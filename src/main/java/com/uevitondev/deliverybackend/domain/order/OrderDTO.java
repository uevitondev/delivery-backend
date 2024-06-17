package com.uevitondev.deliverybackend.domain.order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderDTO implements Serializable {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OrderStatus status;
    private OrderPayment paymentMethod;
    private Double total;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
        this.status = order.getStatus();
        this.paymentMethod = order.getPaymentMethod();
        this.total = order.getTotal();
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderPayment getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(OrderPayment paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

}
