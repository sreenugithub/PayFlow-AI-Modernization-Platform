package com.payflow.soa_modernization_service.reader;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SoaProjectData {

    private String compositeName;

    private String compositeXml;

    private String bpelContent;

    private String wsdlContent;

    private String xsdContent;

    private String migrationGuidelines;
}