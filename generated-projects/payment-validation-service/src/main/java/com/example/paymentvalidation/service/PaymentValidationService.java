package com.example.paymentvalidation.service;

import com.example.paymentvalidation.client.AccountValidationClient;
import com.example.paymentvalidation.dto.AccountValidationResponseDto;
import com.example.paymentvalidation.dto.PaymentValidationRequestDto;
import com.example.paymentvalidation.dto.PaymentValidationResponseDto;
import com.example.paymentvalidation.exception.PaymentValidationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentValidationService {

    private final AccountValidationClient accountValidationClient;

    public PaymentValidationService(AccountValidationClient accountValidationClient) {
        this.accountValidationClient = accountValidationClient;
    }

    public PaymentValidationResponseDto validatePayment(PaymentValidationRequestDto request) {
        if (request.getPaymentReference() == null || request.getPaymentReference().isBlank()) {
            throw new PaymentValidationException("paymentReference must not be blank");
        }
        if (request.getPaymentType() == null || request.getPaymentType().isBlank()) {
            throw new PaymentValidationException("paymentType must not be blank");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentValidationException("amount must be greater than zero");
        }

        AccountValidationResponseDto accountValidation = accountValidationClient.validateAccount(
                request.getPaymentReference(),
                request.getAmount()
        );

        boolean valid = accountValidation.isAccountActive() && accountValidation.isSufficientFunds();
        String message = valid ? "Payment validated successfully" : "Payment validation failed";

        return new PaymentValidationResponseDto(
                request.getPaymentReference(),
                valid,
                message,
                accountValidation.getAccountStatus()
        );
    }
}
