package com.uevitondev.deliverybackend.domain.seller;

import com.uevitondev.deliverybackend.domain.store.Store;
import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_seller")
@PrimaryKeyJoinColumn(name = "user_id")
public class Seller extends User {

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private final Set<Store> stores = new HashSet<>();

    public Seller() {
    }

    public Seller(String firstName, String lastName, String username, String password) {
        super(firstName, lastName, username, password);
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
