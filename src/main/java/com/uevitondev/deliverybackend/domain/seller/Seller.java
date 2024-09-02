package com.uevitondev.deliverybackend.domain.seller;

import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_seller")
@PrimaryKeyJoinColumn(name = "user_id")
public class Seller extends User {

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private final Set<Store> stores = new HashSet<>();

    public Seller() {
    }

    public Seller(
            UUID id,
            String firstName,
            String lastName,
            String username,
            String password
    ) {
        super(id, firstName, lastName, username, password);
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());

    }

    public Set<Store> getStores() {
        return stores;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
