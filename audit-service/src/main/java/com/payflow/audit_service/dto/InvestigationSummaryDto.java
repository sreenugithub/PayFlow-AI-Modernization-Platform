package com.payflow.audit_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestigationSummaryDto {

    private String paymentReference;

    private String paymentStatus;

    private String investigationStatus;

    private String healthScore;

    private Integer totalEvents;

    private Integer totalDltEvents;
}