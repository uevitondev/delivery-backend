package com.uevitondev.deliverybackend.domain.store;

import com.uevitondev.deliverybackend.domain.address.StoreAddress;
import com.uevitondev.deliverybackend.domain.order.Order;
import com.uevitondev.deliverybackend.domain.product.Product;
import com.uevitondev.deliverybackend.domain.seller.Seller;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_store")
public class Store implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;
    @OneToMany(mappedBy = "store")
    private final Set<Product> products = new HashSet<>();
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final Set<Order> orders = new HashSet<>();
    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL)
    private StoreAddress address;

    public Store() {
    }

    public Store(String name, Seller seller) {
        this.name = name;
        this.seller = seller;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public StoreAddress getAddress() {
        return address;
    }

    public void setAddress(StoreAddress address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store pizzeria = (Store) o;
        return Objects.equals(id, pizzeria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
