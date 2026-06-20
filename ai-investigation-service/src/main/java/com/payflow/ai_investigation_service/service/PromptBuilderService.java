package com.payflow.ai_investigation_service.service;

import com.payflow.ai_investigation_service.dto.InvestigationRequest;
import com.payflow.ai_investigation_service.dto.openai.IncidentHistoryRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptBuilderService {

    public String buildPrompt(InvestigationRequest request, String ownerTeam, String severity, String recommendedAction){
        return """
You are an enterprise payment operations investigation assistant.

Analyze the payment investigation context below.

Domain: %s
Reference ID: %s
Business Status: %s
Investigation Status: %s
Root Cause: %s
Suggested Action: %s
Timeline: %s
Resolved Owner Team: %s
Resolved Severity: %s

Resolved Recommended Action: %s
Return ONLY valid JSON.
Do not include markdown.
Do not include explanation outside JSON.

JSON format:
{
  "summary": "",
  "probableCause": "",
  "businessImpact": "",
  "recommendedAction": "",
  "severity": "LOW|MEDIUM|HIGH|CRITICAL",
  "ownerTeam": "",
  "confidence": "LOW|MEDIUM|HIGH"
}
""".formatted(
                request.getDomain(),
                request.getReferenceId(),
                request.getBusinessStatus(),
                request.getInvestigationStatus(),
                request.getRootCause(),
                request.getSuggestedAction(),
                request.getTimeline(),
                ownerTeam,
                severity,
                recommendedAction
        );
    }
// -----------------------------

    public String buildIncidentHistoryPrompt(
            IncidentHistoryRequest request,
            List<String> topRootCauses,
            List<String> topOwnerTeams,
            String severityTrend) {

        return """
You are an enterprise operations intelligence assistant.

Analyze the incident history below.

Total Incidents: %d

Top Root Causes:
%s

Top Owner Teams:
%s

Severity Trend:
%s

Last 50 Incidents:
%s

Provide ONLY JSON.

{
  "aiSummary": "",
  "recommendation": ""
}
"""
                .formatted(
                        request.getIncidents().size(),
                        topRootCauses,
                        topOwnerTeams,
                        severityTrend,
                        formatIncidents(request)
                );
    }

    private String formatIncidents(
            IncidentHistoryRequest request) {

        return request.getIncidents()
                .stream()
                .map(incident ->
                        "- paymentReference=%s, paymentStatus=%s, investigationStatus=%s, rootCause=%s, ownerTeam=%s, severity=%s"
                                .formatted(
                                        incident.getPaymentReference(),
                                        incident.getPaymentStatus(),
                                        incident.getInvestigationStatus(),
                                        incident.getRootCause(),
                                        incident.getOwnerTeam(),
                                        incident.getSeverity()))
                .toList()
                .toString();
    }
}
