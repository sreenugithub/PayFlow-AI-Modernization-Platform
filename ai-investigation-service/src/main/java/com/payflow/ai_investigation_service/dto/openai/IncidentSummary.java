package com.payflow.ai_investigation_service.dto.openai;
import lombok.*;

import java.util.List;
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