package com.example.paymentvalidation.dto;

public class PaymentValidationResponseDto {

    private String paymentReference;
    private boolean valid;
    private String message;
    private String accountStatus;

    public PaymentValidationResponseDto() {
    }

    public PaymentValidationResponseDto(String paymentReference, boolean valid, String message, String accountStatus) {
        this.paymentReference = paymentReference;
        this.valid = valid;
        this.message = message;
        this.accountStatus = accountStatus;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
