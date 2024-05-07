package com.uevitondev.deliverybackend.domain.model;

import com.uevitondev.deliverybackend.domain.enums.OrderStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Double total;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final Set<OrderItem> orderItems = new HashSet<>();

    public Order() {
    }

    public Order(OrderStatus status, Customer customer, Set<OrderItem> orderItems) {
        this.status = status;
        this.customer = customer;
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        addOrderItemsAndCalculateTotal(orderItems);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItemsAndCalculateTotal(Set<OrderItem> orderItems) {
        this.orderItems.addAll(orderItems);
        calculateOrderTotal();
    }

    public void calculateOrderTotal() {
        double sum = 0.0;
        for (OrderItem orderItem : this.orderItems) {
            sum += orderItem.getTotal();
        }
        this.total = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
