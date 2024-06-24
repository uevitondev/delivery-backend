package com.uevitondev.deliverybackend.domain.address;

import java.io.Serializable;
import java.util.UUID;

public class AddressDTO implements Serializable {
    private UUID id;
    private String street;
    private Integer number;
    private String district;
    private String city;
    private String uf;
    private String complement;
    private String zipCode;


    public AddressDTO(UUID id, String street, Integer number, String district, String city, String uf, String complement, String zipCode) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.district = district;
        this.city = city;
        this.uf = uf;
        this.complement = complement;
        this.zipCode = zipCode;
    }

    public AddressDTO(Address address) {
        this.id = address.getId();
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.district = address.getDistrict();
        this.city = address.getCity();
        this.uf = address.getUf();
        this.complement = address.getComplement();
        this.zipCode = address.getZipCode();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
