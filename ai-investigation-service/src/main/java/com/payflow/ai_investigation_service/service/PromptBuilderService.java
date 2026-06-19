package com.payflow.ai_investigation_service.service;

import com.payflow.ai_investigation_service.dto.InvestigationRequest;
import org.springframework.stereotype.Service;

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


}
