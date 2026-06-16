package com.payflow.audit_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "payment_audit_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAuditEvent {

    @Id
    private String id;

    private String paymentId;
    private String paymentReference;
    private String eventType;
    private String paymentType;
    private Double amount;
    private String status;
    private LocalDateTime receivedAt;
    private String oldStatus;

    private String newStatus;
}