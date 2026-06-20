package com.payflow.ai_investigation_service.dto.openai;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentHistoryRequest {

    private List<IncidentSummary> incidents;
}