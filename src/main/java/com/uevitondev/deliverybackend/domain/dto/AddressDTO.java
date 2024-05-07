package com.uevitondev.deliverybackend.domain.dto;

import com.uevitondev.deliverybackend.domain.model.Address;

import java.io.Serializable;
import java.util.UUID;

public class AddressDTO implements Serializable {
    private UUID id;
    private String zipCode;
    private String uf;
    private String city;
    private String district;
    private String street;
    private String complement;
    private Integer number;

    public AddressDTO(UUID id, String zipCode, String uf, String city, String district, String street, Integer number) {
        this.id = id;
        this.zipCode = zipCode;
        this.uf = uf;
        this.city = city;
        this.district = district;
        this.street = street;
        this.number = number;
    }

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.zipCode = address.getZipCode();
        this.uf = address.getUf();
        this.city = address.getCity();
        this.district = address.getDistrict();
        this.street = address.getStreet();
        this.complement = address.getComplement();
        this.number = address.getNumber();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
