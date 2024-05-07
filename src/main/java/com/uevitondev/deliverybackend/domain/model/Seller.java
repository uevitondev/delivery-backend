package com.uevitondev.deliverybackend.domain.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_seller")
@PrimaryKeyJoinColumn(name = "user_id")
public class Seller extends User {
    private String fantasyName;
    private String cnpj;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final Set<Store> stores = new HashSet<>();

    public Seller() {
    }

    public Seller(String firstName, String lastName, String username, String password, String fantasyName, String cnpj) {
        super(firstName, lastName, username, password);
        this.fantasyName = fantasyName;
        this.cnpj = cnpj;
    }

    public String getFantasyName() {
        return fantasyName;
    }

    public void setFantasyName(String fantasyName) {
        this.fantasyName = fantasyName;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Set<Store> getStores() {
        return stores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seller seller = (Seller) o;
        return Objects.equals(fantasyName, seller.fantasyName) && Objects.equals(cnpj, seller.cnpj) && Objects.equals(stores, seller.stores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fantasyName, cnpj, stores);
    }
}
