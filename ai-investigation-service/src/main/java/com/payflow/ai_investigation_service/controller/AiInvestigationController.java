package com.payflow.ai_investigation_service.controller;

import com.payflow.ai_investigation_service.dto.AiInvestigationResponse;
import com.payflow.ai_investigation_service.dto.InvestigationRequest;
import com.payflow.ai_investigation_service.dto.openai.IncidentHistoryAnalysisResponse;
import com.payflow.ai_investigation_service.dto.openai.IncidentHistoryRequest;
import com.payflow.ai_investigation_service.service.AiInvestigationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiInvestigationController {

    private final AiInvestigationService aiInvestigationService;

    @PostMapping("/investigate")
    public AiInvestigationResponse investigate(
            @RequestBody InvestigationRequest request) {

        return aiInvestigationService.investigate(request);
    }

    @PostMapping("/incident-history-analysis")
    public IncidentHistoryAnalysisResponse analyzeHistory(
            @RequestBody IncidentHistoryRequest request) {

        return aiInvestigationService
                .analyzeHistory(request);
    }
}