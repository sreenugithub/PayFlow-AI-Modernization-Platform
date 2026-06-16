package com.payflow.payment_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {

    private String sourceAccount;

    private String destinationAccount;

    private Double amount;

    private String currency;

    private String paymentType;
}