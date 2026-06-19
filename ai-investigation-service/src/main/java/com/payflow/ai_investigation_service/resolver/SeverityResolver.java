package com.payflow.ai_investigation_service.resolver;

import com.payflow.ai_investigation_service.model.InvestigationContext;
import org.springframework.stereotype.Service;

@Service
public class SeverityResolver {

    public String resolveSeverity(InvestigationContext context) {

        if ("ATTENTION_REQUIRED".equalsIgnoreCase(context.getInvestigationStatus())) {
            return "CRITICAL";
        }

        if ("STUCK".equalsIgnoreCase(context.getPaymentStatus())) {
            return "HIGH";
        }

        if ("PROCESSING".equalsIgnoreCase(context.getPaymentStatus())) {
            return "MEDIUM";
        }

        return "LOW";
    }
}