package com.uevitondev.deliverybackend.domain.payment;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_payment_method")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethodName name;
    @Column(nullable = false)
    private String description;

    public PaymentMethod() {
    }

    public PaymentMethod(UUID id, PaymentMethodName name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PaymentMethodName getName() {
        return name;
    }

    public void setName(PaymentMethodName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
