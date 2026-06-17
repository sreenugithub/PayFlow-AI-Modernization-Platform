package com.payflow.audit_service.repository;

import com.payflow.audit_service.entity.DltEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DltEventRepository
        extends MongoRepository<DltEvent, String> {

    List<DltEvent>
    findByPaymentReference(
            String paymentReference);

    List<DltEvent>
    findByPaymentReferenceContainingIgnoreCase(
            String paymentReference);
}
