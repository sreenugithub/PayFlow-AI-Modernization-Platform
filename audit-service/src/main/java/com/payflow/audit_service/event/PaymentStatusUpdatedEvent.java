package com.payflow.audit_service.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentStatusUpdatedEvent {

    private String paymentId;
    private String paymentReference;
    private String oldStatus;
    private String newStatus;
}