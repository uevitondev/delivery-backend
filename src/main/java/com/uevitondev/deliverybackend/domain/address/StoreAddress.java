package com.uevitondev.deliverybackend.domain.address;

import com.uevitondev.deliverybackend.domain.store.Store;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_store_address")
public class StoreAddress extends Address implements Serializable {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    protected StoreAddress() {
        super();
    }

    public StoreAddress(String street, Integer number, String district, String city, String uf, String complement, String zipCode, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(street, number, district, city, uf, complement, zipCode, createdAt, updatedAt);
    }


    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
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
