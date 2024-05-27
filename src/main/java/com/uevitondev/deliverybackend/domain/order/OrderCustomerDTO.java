package com.uevitondev.deliverybackend.domain.order;

import com.uevitondev.deliverybackend.domain.enums.OrderStatus;
import com.uevitondev.deliverybackend.domain.orderitem.OrderItemDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderCustomerDTO implements Serializable {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OrderStatus status;
    private Double total;
    private UUID userId;
    private UUID storeId;
    private UUID addressId;
    private final List<OrderItemDTO> orderItems = new ArrayList<>();

    public OrderCustomerDTO(Order order) {
        this.id = order.getId();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
        this.status = order.getStatus();
        this.total = order.getTotal();
        this.userId = order.getCustomer().getId();
        this.storeId = order.getStore().getId();
        this.addressId = order.getAddress().getId();
        order.getOrderItems().forEach(orderItem -> orderItems.add(new OrderItemDTO(orderItem)));
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }
}
