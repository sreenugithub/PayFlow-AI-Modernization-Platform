package com.payflow.soa_modernization_service.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.soa_modernization_service.model.GeneratedProject;
import com.payflow.soa_modernization_service.model.SoaMigrationRequest;
import com.payflow.soa_modernization_service.model.SoaMigrationResponse;
import com.payflow.soa_modernization_service.prompt.SoaMigrationPromptBuilder;
import com.payflow.soa_modernization_service.reader.SoaProjectData;
import com.payflow.soa_modernization_service.reader.SoaProjectReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SoaModernizationService {

    private final SoaProjectReader soaProjectReader;

    private final SoaMigrationPromptBuilder promptBuilder;

    private final OpenAIService openAIService;

    private final ObjectMapper objectMapper;

    private final ProjectWriterService projectWriterService;

    public SoaMigrationResponse migrate(
            SoaMigrationRequest request) {

        try {
            SoaProjectData data =
                    soaProjectReader.readProject(
                            request.getSoaProjectPath());

            String prompt =
                    promptBuilder.buildPrompt(data);

            String aiResponse =
                    openAIService.generateProjectJson(prompt);

            String cleanJson =
                    cleanJson(aiResponse);

            GeneratedProject project =
                    objectMapper.readValue(
                            cleanJson,
                            GeneratedProject.class);

            String outputPath =
                    projectWriterService.writeProject(project);

            return SoaMigrationResponse.builder()
                    .projectName(project.getProjectName())
                    .outputPath(outputPath)
                    .filesGenerated(
                            project.getFiles() == null
                                    ? 0
                                    : project.getFiles().size())
                    .status("SUCCESS")
                    .build();

        } catch (Exception ex) {
            return SoaMigrationResponse.builder()
                    .status("FAILED: " + ex.getMessage())
                    .build();
        }
    }

    private String cleanJson(String response) {

        if (response == null) {
            return "";
        }

        return response
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}