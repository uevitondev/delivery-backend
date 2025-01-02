package com.uevitondev.deliverybackend.domain.payment;

import com.uevitondev.deliverybackend.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }


    public List<PaymentMethod> getAllPaymentMethods(){
        return this.paymentMethodRepository.findAll();
    }

    public PaymentMethod findPaymentMethodById(UUID id) {
        return paymentMethodRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("payment method not found by id")
        );
    }


    public PaymentMethod findPaymentMethodByName(String paymentMethodName) {
        return paymentMethodRepository.findByName(paymentMethodName).orElseThrow(
                () -> new ResourceNotFoundException("payment method not found by name")
        );
    }
}
