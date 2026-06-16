package com.payflow.payment_service.repository;

import com.payflow.payment_service.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository
        extends MongoRepository<Payment, String> {
}