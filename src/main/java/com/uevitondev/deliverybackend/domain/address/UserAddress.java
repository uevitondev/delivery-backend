package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_user_address")
public class UserAddress extends Address implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserAddress() {
    }

    public UserAddress(String zipCode, String uf, String city, String district, String street, Integer number) {
        super(zipCode, uf, city, district, street, number);
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
