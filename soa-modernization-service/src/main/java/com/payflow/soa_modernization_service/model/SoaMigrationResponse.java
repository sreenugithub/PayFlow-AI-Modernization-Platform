package com.payflow.soa_modernization_service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SoaMigrationResponse {

    private String projectName;

    private String outputPath;

    private Integer filesGenerated;

    private String status;
}