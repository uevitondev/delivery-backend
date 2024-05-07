package com.uevitondev.deliverybackend.domain.dto;

import com.uevitondev.deliverybackend.domain.enums.OrderStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OrderDTO implements Serializable {
    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private OrderStatus status;
    private Double total;
    private UUID userId;
    private UUID storeId;
    private UUID addressId;
    private final Set<OrderItemDTO> orderItems = new HashSet<>();

    public OrderDTO() {
    }

    public OrderDTO(UUID id, LocalDateTime createdAt, LocalDateTime updateAt, OrderStatus status) {
        this.id = id;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.createdAt = order.getCreatedAt();
        this.updateAt = order.getUpdateAt();
        this.status = order.getStatus();
        this.total = order.getTotal();
        this.userId = order.getCustomer().getId();
        this.storeId = order.getStore().getId();
        this.addressId = order.getAddress().getId();
        order.getOrderItems().forEach(orderItem -> this.orderItems.add(new OrderItemDTO(orderItem)));
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

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
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

    public Set<OrderItemDTO> getOrderItems() {
        return orderItems;
    }
}
