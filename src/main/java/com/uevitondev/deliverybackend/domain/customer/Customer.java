package com.uevitondev.deliverybackend.domain.customer;

import com.uevitondev.deliverybackend.domain.order.Order;
import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_customer")
@PrimaryKeyJoinColumn(name = "customer_id")
public class Customer extends User {

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private final Set<Order> orders = new HashSet<>();

    public Customer() {
    }

    public Customer(String firstName, String lastName, String username, String password) {
        super(firstName, lastName, username, password);
    }

    public Set<Order> getOrders() {
        return orders;
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
