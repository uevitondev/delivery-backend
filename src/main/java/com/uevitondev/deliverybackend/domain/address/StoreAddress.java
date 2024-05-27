package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.store.Store;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_store_address")
public class StoreAddress extends Address implements Serializable {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public StoreAddress() {
    }

    public StoreAddress(Store store) {
        this.store = store;
    }

    public StoreAddress(String zipCode, String uf, String city, String district, String street, Integer number, Store store) {
        super(zipCode, uf, city, district, street, number);
        this.store = store;
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
