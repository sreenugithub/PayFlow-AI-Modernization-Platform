package com.example.paymentvalidation.dto;

public class AccountValidationResponse {

    private boolean valid;
    private String reason;

    public AccountValidationResponse() {
    }

    public AccountValidationResponse(boolean valid, String reason) {
        this.valid = valid;
        this.reason = reason;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
