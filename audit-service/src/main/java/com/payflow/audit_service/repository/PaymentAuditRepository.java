package com.payflow.audit_service.repository;

import com.payflow.audit_service.entity.PaymentAuditEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentAuditRepository
        extends MongoRepository<PaymentAuditEvent, String> {

    List<PaymentAuditEvent>
    findByPaymentReferenceOrderByReceivedAtAsc(
            String paymentReference);

    List<PaymentAuditEvent>
    findByPaymentReferenceContainingIgnoreCase(
            String paymentReference);

    List<PaymentAuditEvent>
    findTop50ByOrderByReceivedAtDesc();

}
