package com.payflow.soa_modernization_service.model;

import lombok.Data;

import java.util.List;

@Data
public class GeneratedProject {

    private String projectName;
    private String description;
    private List<GeneratedFile> files;
}