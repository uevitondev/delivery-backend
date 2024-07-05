package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user_address")
public class UserAddress extends Address implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserAddress() {
        super();
    }

    public UserAddress(String street, Integer number, String district, String city, String uf, String complement, String zipCode, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(street, number, district, city, uf, complement, zipCode, createdAt, updatedAt);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
