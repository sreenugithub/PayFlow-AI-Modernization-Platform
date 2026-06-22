package com.payflow.soa_modernization_service.controller;


import com.payflow.soa_modernization_service.model.SoaMigrationRequest;
import com.payflow.soa_modernization_service.model.SoaMigrationResponse;
import com.payflow.soa_modernization_service.service.SoaModernizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/soa")
@RequiredArgsConstructor
public class SoaModernizationController {

    private final SoaModernizationService service;

    @PostMapping("/migrate")
    public SoaMigrationResponse migrate(
            @RequestBody SoaMigrationRequest request) {

        return service.migrate(request);
    }
}