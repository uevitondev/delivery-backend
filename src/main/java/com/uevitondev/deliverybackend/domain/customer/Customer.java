package com.uevitondev.deliverybackend.domain.customer;

import com.uevitondev.deliverybackend.domain.order.Order;
import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_customer")
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {
    private String cpf;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final Set<Order> orders = new HashSet<>();

    public Customer() {
    }

    public Customer(String firstName, String lastName, String username, String password, String cpf) {
        super(firstName, lastName, username, password);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(cpf, customer.cpf) && Objects.equals(orders, customer.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf, orders);
    }
}
