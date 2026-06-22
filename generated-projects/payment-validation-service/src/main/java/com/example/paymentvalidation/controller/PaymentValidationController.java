package com.example.paymentvalidation.controller;

import com.example.paymentvalidation.dto.PaymentValidationRequestDto;
import com.example.paymentvalidation.dto.PaymentValidationResponseDto;
import com.example.paymentvalidation.service.PaymentValidationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentValidationController {

    private final PaymentValidationService paymentValidationService;

    public PaymentValidationController(PaymentValidationService paymentValidationService) {
        this.paymentValidationService = paymentValidationService;
    }

    @PostMapping("/validate")
    public ResponseEntity<PaymentValidationResponseDto> validatePayment(
            @Valid @RequestBody PaymentValidationRequestDto request) {
        return ResponseEntity.ok(paymentValidationService.validatePayment(request));
    }
}
