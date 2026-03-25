package com.yourcompany.bitcoinpayment.controller;

import com.yourcompany.bitcoinpayment.dto.CreatePaymentRequest;
import com.yourcompany.bitcoinpayment.dto.PaymentResponse;
import com.yourcompany.bitcoinpayment.dto.PaymentStatusResponse;
import com.yourcompany.bitcoinpayment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.create(request);
    }

    @GetMapping("/{id}")
    public PaymentResponse get(@PathVariable UUID id) {
        return paymentService.get(id);
    }

    @GetMapping("/{id}/status")
    public PaymentStatusResponse status(@PathVariable UUID id) {
        return paymentService.status(id);
    }
}
