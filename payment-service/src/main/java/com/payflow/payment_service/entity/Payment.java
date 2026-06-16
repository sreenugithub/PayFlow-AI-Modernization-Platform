package com.payflow.payment_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    private String id;

    private String paymentReference;

    private String sourceAccount;

    private String destinationAccount;

    private Double amount;

    private String currency;

    private String paymentType;

    private String status;

    private LocalDateTime createdAt;
}