package com.payflow.audit_service.dto.ai;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentSummary {

    private String paymentReference;

    private String paymentStatus;

    private String investigationStatus;

    private String rootCause;

    private String ownerTeam;

    private String severity;
}