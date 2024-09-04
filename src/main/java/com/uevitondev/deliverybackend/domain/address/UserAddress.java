package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.user.User;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_user_address")
public class UserAddress extends Address implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserAddress() {
        super();
    }

    public UserAddress(
            UUID id,
            String name,
            String phoneNumber,
            String street,
            Integer number,
            String district,
            String city,
            String uf,
            String complement,
            String zipCode
    ) {
        super(
                id,
                name,
                phoneNumber,
                street,
                number,
                district,
                city,
                uf,
                complement,
                zipCode,
                LocalDateTime.now(),
                LocalDateTime.now()
        );


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
