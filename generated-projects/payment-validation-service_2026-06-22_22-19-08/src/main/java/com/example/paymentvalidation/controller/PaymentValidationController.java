package com.example.paymentvalidation.controller;

import com.example.paymentvalidation.dto.PaymentValidationRequest;
import com.example.paymentvalidation.dto.PaymentValidationResponse;
import com.example.paymentvalidation.service.PaymentValidationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/payments")
public class PaymentValidationController {

    private final PaymentValidationService paymentValidationService;

    public PaymentValidationController(PaymentValidationService paymentValidationService) {
        this.paymentValidationService = paymentValidationService;
    }

    @PostMapping("/validate")
    public ResponseEntity<PaymentValidationResponse> validatePayment(@Valid @RequestBody PaymentValidationRequest request) {
        return ResponseEntity.ok(paymentValidationService.validatePayment(request));
    }
}
