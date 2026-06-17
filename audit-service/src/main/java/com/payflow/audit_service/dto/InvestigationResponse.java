package com.payflow.audit_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestigationResponse {

    private String paymentReference;

    private String paymentStatus;

    private String investigationStatus;

    private String healthScore;

    private String rootCause;

    private String suggestedAction;

    private String investigationSummary;

    private Integer totalEvents;

    private Integer totalDltEvents;

    private List<TimelineEventDto> timeline;
}