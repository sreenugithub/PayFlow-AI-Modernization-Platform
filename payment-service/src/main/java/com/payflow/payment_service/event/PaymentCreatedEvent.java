package com.payflow.payment_service.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreatedEvent {

    private String paymentId;

    private String paymentReference;

    private Double amount;

    private String paymentType;

    private String status;
}