package com.payflow.soa_modernization_service.reader;

import java.nio.file.Files;
import java.nio.file.Path;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.soa_modernization_service.model.GeneratedProject;
import com.payflow.soa_modernization_service.prompt.SoaMigrationPromptBuilder;
import com.payflow.soa_modernization_service.service.OpenAIService;
import com.payflow.soa_modernization_service.service.ProjectWriterService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SoaProjectReader {

    private final SoaMigrationPromptBuilder promptBuilder;
    private final  OpenAIService openAIService;
    private final ProjectWriterService projectWriterService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void test() throws JsonProcessingException {

        SoaProjectData data =

                readProject(

                        "soa-modernization-lab/PaymentValidationComposite");

        String prompt =
                promptBuilder.buildPrompt(data);

        //System.out.println(prompt);
        String aiResponse =
                openAIService.generateProjectJson(prompt);
        System.out.println(" ************     aiResponse    ********** ");

        //System.out.println(aiResponse);

        GeneratedProject project = objectMapper.readValue( aiResponse, GeneratedProject.class);

        System.out.println(project.getProjectName());

        System.out.println(project.getFiles().size());


        projectWriterService.writeProject(project);
    }
    public SoaProjectData readProject(String projectPath) {

        try {

            Path rootPath =
                    resolvePath(projectPath);

            String compositeXml = readFile(
                    rootPath.resolve("composite.xml"));

            String bpelContent = readFirstFile(
                    rootPath.resolve("bpel"),
                    ".bpel");

            String wsdlContent = readFirstFile(
                    rootPath.resolve("wsdl"),
                    ".wsdl");

            String xsdContent = readFirstFile(
                    rootPath.resolve("xsd"),
                    ".xsd");

            String migrationGuidelines = readFile(
                    resolvePath("docs/soa-migration-guidelines.md"));

            return SoaProjectData.builder()
                    .compositeName(rootPath.getFileName().toString())
                    .compositeXml(compositeXml)
                    .bpelContent(bpelContent)
                    .wsdlContent(wsdlContent)
                    .xsdContent(xsdContent)
                    .migrationGuidelines(migrationGuidelines)
                    .build();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to read SOA project : "
                            + projectPath,
                    ex);
        }
    }

    private String readFile(Path path) throws IOException {

        if (!Files.exists(path)) {
            return "";
        }

        return Files.readString(path);
    }

    private Path resolvePath(String path) {

        Path currentDirectoryPath =
                Path.of(path);

        if (Files.exists(currentDirectoryPath)) {
            return currentDirectoryPath;
        }

        Path parentDirectoryPath =
                Path.of("..").resolve(path).normalize();

        if (Files.exists(parentDirectoryPath)) {
            return parentDirectoryPath;
        }

        return currentDirectoryPath;
    }

    private String readFirstFile(
            Path directory,
            String extension) throws IOException {

        if (!Files.exists(directory)) {
            return "";
        }

        try (Stream<Path> files = Files.list(directory)) {

            Path file = files
                    .filter(p -> p.toString().endsWith(extension))
                    .findFirst()
                    .orElse(null);

            if (file == null) {
                return "";
            }

            return Files.readString(file);
        }
    }
}
