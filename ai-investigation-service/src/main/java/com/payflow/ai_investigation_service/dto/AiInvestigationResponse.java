package com.payflow.ai_investigation_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiInvestigationResponse {

    private String summary;

    private String probableCause;

    private String businessImpact;

    private String recommendedAction;

    private String severity;

    private String ownerTeam;

    private String confidence;
}