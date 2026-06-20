package com.payflow.audit_service.controller;

import com.payflow.audit_service.dto.DashboardSummaryResponse;
import com.payflow.audit_service.dto.InvestigationResponse;
import com.payflow.audit_service.dto.InvestigationSummaryDto;
import com.payflow.audit_service.dto.ai.AiInvestigationResponse;
import com.payflow.audit_service.dto.ai.IncidentHistoryAnalysisResponse;
import com.payflow.audit_service.service.InvestigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/investigation")
@RequiredArgsConstructor
public class InvestigationController {

    private final InvestigationService investigationService;

    @GetMapping("/{paymentReference}")
    public InvestigationResponse investigate(
            @PathVariable String paymentReference) {

        return investigationService.investigate(
                paymentReference);
    }

    @GetMapping
    public List<InvestigationSummaryDto> getAllInvestigations() {

        return investigationService.getAllInvestigations();
    }

    @GetMapping("/dashboard-summary")
    public DashboardSummaryResponse dashboardSummary() {

        return investigationService.getDashboardSummary();
    }

    @GetMapping("/critical")
    public List<InvestigationSummaryDto> critical() {
        return investigationService.getAllInvestigations()
                .stream()
                .filter(i -> "CRITICAL".equals(i.getHealthScore()))
                .toList();
    }

    @GetMapping("/warning")

    public List<InvestigationSummaryDto> warning() {

        return investigationService.getAllInvestigations()

                .stream()

                .filter(i -> "WARNING".equals(i.getHealthScore()))

                .toList();

    }

    @GetMapping("/healthy")

    public List<InvestigationSummaryDto> healthy() {

        return investigationService.getAllInvestigations()

                .stream()

                .filter(i -> "HEALTHY".equals(i.getHealthScore()))

                .toList();

    }

    @GetMapping("/search")
    public List<InvestigationSummaryDto> search(
            @RequestParam String keyword) {

        return investigationService.search(keyword);
    }

    @GetMapping("/{paymentReference}/ai-analysis")
    public AiInvestigationResponse aiAnalysis(
            @PathVariable String paymentReference) {

        return investigationService.aiAnalysis(
                paymentReference);
    }

    @GetMapping("/incident-history-analysis")
    public IncidentHistoryAnalysisResponse
    analyzeIncidentHistory() {

        return investigationService
                .analyzeIncidentHistory();
    }
}
