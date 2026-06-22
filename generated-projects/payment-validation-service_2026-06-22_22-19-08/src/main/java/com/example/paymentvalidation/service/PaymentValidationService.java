package com.example.paymentvalidation.service;

import com.example.paymentvalidation.client.AccountValidationClient;
import com.example.paymentvalidation.dto.AccountValidationRequest;
import com.example.paymentvalidation.dto.AccountValidationResponse;
import com.example.paymentvalidation.dto.PaymentValidationRequest;
import com.example.paymentvalidation.dto.PaymentValidationResponse;
import com.example.paymentvalidation.exception.PaymentValidationException;
import org.springframework.stereotype.Service;

@Service
public class PaymentValidationService {

    private final AccountValidationClient accountValidationClient;

    public PaymentValidationService(AccountValidationClient accountValidationClient) {
        this.accountValidationClient = accountValidationClient;
    }

    public PaymentValidationResponse validatePayment(PaymentValidationRequest request) {
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new PaymentValidationException("Amount must be greater than zero");
        }
        if (request.getPaymentReference() == null || request.getPaymentReference().isBlank()) {
            throw new PaymentValidationException("Payment reference is required");
        }
        if (request.getPaymentType() == null || request.getPaymentType().isBlank()) {
            throw new PaymentValidationException("Payment type is required");
        }

        AccountValidationRequest accountRequest = new AccountValidationRequest(
                request.getPaymentReference(),
                request.getPaymentType(),
                request.getAmount()
        );

        AccountValidationResponse accountResponse = accountValidationClient.validateAccount(accountRequest);
        boolean validated = accountResponse != null && accountResponse.isValid();

        if (!validated) {
            return new PaymentValidationResponse(
                    request.getPaymentReference(),
                    "REJECTED",
                    accountResponse != null && accountResponse.getReason() != null
                            ? accountResponse.getReason()
                            : "Account validation failed",
                    false
            );
        }

        return new PaymentValidationResponse(
                request.getPaymentReference(),
                "APPROVED",
                "Payment validated successfully",
                true
        );
    }
}
