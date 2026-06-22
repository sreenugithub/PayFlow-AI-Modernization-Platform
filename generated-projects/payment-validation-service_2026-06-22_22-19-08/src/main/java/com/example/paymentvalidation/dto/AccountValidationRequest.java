package com.example.paymentvalidation.dto;

public class AccountValidationRequest {

    private String paymentReference;
    private String paymentType;
    private Double amount;

    public AccountValidationRequest() {
    }

    public AccountValidationRequest(String paymentReference, String paymentType, Double amount) {
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
