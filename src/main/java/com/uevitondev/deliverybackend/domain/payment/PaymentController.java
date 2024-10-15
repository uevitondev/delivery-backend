package com.uevitondev.deliverybackend.domain.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/methods")
    public ResponseEntity<List<PaymentMethodDTO>> paymentMethods() {
        return ResponseEntity.ok().body(
                this.paymentService.getAllPaymentMethods().stream().map(PaymentMethodDTO::new).toList()
        );
    }


}
