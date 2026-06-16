package com.payflow.audit_service.repository;

import com.payflow.audit_service.entity.PaymentAuditEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentAuditRepository
        extends MongoRepository<PaymentAuditEvent, String> {
}