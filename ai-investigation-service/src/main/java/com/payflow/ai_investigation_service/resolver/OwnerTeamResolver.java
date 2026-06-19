package com.payflow.ai_investigation_service.resolver;

import com.payflow.ai_investigation_service.model.InvestigationContext;
import org.springframework.stereotype.Service;

@Service
public class OwnerTeamResolver {

    public String resolveOwnerTeam(InvestigationContext context) {

        if (context.getRootCause() == null) {
            return "Payment Operations";
        }

        String cause = context.getRootCause().toLowerCase();

        if (cause.contains("kafka") || cause.contains("dlt")) {
            return "Messaging Team";
        }

        if (cause.contains("timeout")) {
            return "Settlement Team";
        }

        if (cause.contains("database") || cause.contains("mongo")) {
            return "Database Team";
        }

        if (cause.contains("authentication")
                || cause.contains("jwt")
                || cause.contains("token")) {
            return "Security Team";
        }

        if (cause.contains("api") || cause.contains("gateway")) {
            return "Platform Team";
        }

        return "Payment Operations";
    }
}