package com.uevitondev.deliverybackend.domain.order;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_order_delivery")
public class OrderDelivery implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    String deliveryName;
    @Column(nullable = false)
    String deliveryPhoneNumber;
    @Column(nullable = false)
    private String deliveryStreet;
    @Column(nullable = false)
    private Integer deliveryNumber;
    @Column(nullable = false)
    private String deliveryDistrict;
    @Column(nullable = false)
    private String deliveryCity;
    @Column(nullable = false)
    private String deliveryUf;
    @Column(nullable = false)
    private String deliveryComplement;
    @Column(nullable = false)
    private String deliveryZipCode;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderDelivery() {
    }

    public OrderDelivery(
            UUID id,
            String deliveryName,
            String deliveryPhoneNumber,
            String deliveryStreet,
            Integer deliveryNumber,
            String deliveryDistrict,
            String deliveryCity,
            String deliveryUf,
            String deliveryComplement,
            String deliveryZipCode
    ) {
        this.id = id;
        this.deliveryName = deliveryName;
        this.deliveryPhoneNumber = deliveryPhoneNumber;
        this.deliveryStreet = deliveryStreet;
        this.deliveryNumber = deliveryNumber;
        this.deliveryDistrict = deliveryDistrict;
        this.deliveryCity = deliveryCity;
        this.deliveryUf = deliveryUf;
        this.deliveryComplement = deliveryComplement;
        this.deliveryZipCode = deliveryZipCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryPhoneNumber() {
        return deliveryPhoneNumber;
    }

    public void setDeliveryPhoneNumber(String deliveryPhoneNumber) {
        this.deliveryPhoneNumber = deliveryPhoneNumber;
    }

    public String getDeliveryStreet() {
        return deliveryStreet;
    }

    public void setDeliveryStreet(String deliveryStreet) {
        this.deliveryStreet = deliveryStreet;
    }

    public Integer getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(Integer deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public String getDeliveryDistrict() {
        return deliveryDistrict;
    }

    public void setDeliveryDistrict(String deliveryDistrict) {
        this.deliveryDistrict = deliveryDistrict;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryUf() {
        return deliveryUf;
    }

    public void setDeliveryUf(String deliveryUf) {
        this.deliveryUf = deliveryUf;
    }

    public String getDeliveryComplement() {
        return deliveryComplement;
    }

    public void setDeliveryComplement(String deliveryComplement) {
        this.deliveryComplement = deliveryComplement;
    }

    public String getDeliveryZipCode() {
        return deliveryZipCode;
    }

    public void setDeliveryZipCode(String deliveryZipCode) {
        this.deliveryZipCode = deliveryZipCode;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
