package com.uevitondev.deliverybackend.domain.model;

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
    private LocalDateTime updateAt;
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;
    @OneToMany(mappedBy = "store")
    private final Set<Product> products = new HashSet<>();
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final Set<Order> orders = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tb_store_address",
            joinColumns = @JoinColumn(name = "store_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private Address address;

    public Store() {
    }

    public Store(String name, Seller seller) {
        this.name = name;
        this.seller = seller;
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
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

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
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
