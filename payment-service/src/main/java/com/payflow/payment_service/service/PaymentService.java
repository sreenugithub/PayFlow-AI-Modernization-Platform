package com.payflow.payment_service.service;

import com.payflow.payment_service.dto.CreatePaymentRequest;
import com.payflow.payment_service.entity.Payment;
import com.payflow.payment_service.event.PaymentCreatedEvent;
import com.payflow.payment_service.producer.PaymentEventPublisher;
import com.payflow.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;

    public Payment createPayment(CreatePaymentRequest request) {

        Payment payment = Payment.builder()
                .paymentReference(UUID.randomUUID().toString())
                .sourceAccount(request.getSourceAccount())
                .destinationAccount(request.getDestinationAccount())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .paymentType(request.getPaymentType())
                .status("PROCESSING")
                .createdAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        PaymentCreatedEvent event =
                PaymentCreatedEvent.builder()
                        .paymentId(savedPayment.getId())
                        .paymentReference(savedPayment.getPaymentReference())
                        .amount(savedPayment.getAmount())
                        .paymentType(savedPayment.getPaymentType())
                        .status(savedPayment.getStatus())
                        .build();

        paymentEventPublisher.publishPaymentCreatedEvent(event);

        return savedPayment;
     }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}