package com.payflow.payment_status_service.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StuckPaymentEvent {

    private String paymentId;

    private String paymentReference;

    private String currentStatus;

    private String reason;

    private Long stuckDurationMinutes;
}