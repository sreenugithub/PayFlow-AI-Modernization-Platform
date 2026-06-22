package com.example.paymentvalidation.dto;

public class PaymentValidationResponse {

    private String paymentReference;
    private String status;
    private String message;
    private boolean accountValidated;

    public PaymentValidationResponse() {
    }

    public PaymentValidationResponse(String paymentReference, String status, String message, boolean accountValidated) {
        this.paymentReference = paymentReference;
        this.status = status;
        this.message = message;
        this.accountValidated = accountValidated;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAccountValidated() {
        return accountValidated;
    }

    public void setAccountValidated(boolean accountValidated) {
        this.accountValidated = accountValidated;
    }
}
