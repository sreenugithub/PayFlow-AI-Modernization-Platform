package com.payflow.ai_investigation_service.resolver;

import com.payflow.ai_investigation_service.model.InvestigationContext;
import org.springframework.stereotype.Service;

@Service
public class RecommendationEngine {

    public String recommend(InvestigationContext context) {

        if (context.getRootCause() == null) {
            return "Continue monitoring.";
        }

        String cause = context.getRootCause().toLowerCase();

        if (cause.contains("kafka") || cause.contains("dlt")) {
            return "Replay failed event from DLT topic and review Kafka consumer logs.";
        }

        if (cause.contains("timeout")) {
            return "Review downstream service availability and retry the payment workflow.";
        }

        if (cause.contains("database") || cause.contains("mongo")) {
            return "Review database connectivity, connection pool usage, and persistence errors.";
        }

        return context.getSuggestedAction() != null
                ? context.getSuggestedAction()
                : "Review operational logs and investigate.";
    }
}