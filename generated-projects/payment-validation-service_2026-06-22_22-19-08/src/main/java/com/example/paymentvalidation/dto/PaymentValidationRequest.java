package com.example.paymentvalidation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PaymentValidationRequest {

    @NotBlank
    private String paymentReference;

    @NotBlank
    private String paymentType;

    @NotNull
    @Positive
    private Double amount;

    public PaymentValidationRequest() {
    }

    public PaymentValidationRequest(String paymentReference, String paymentType, Double amount) {
        this.paymentReference = paymentReference;
        this.paymentType = paymentType;
        this.amount = amount;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
