package com.payflow.soa_modernization_service.service;


import com.payflow.soa_modernization_service.model.GeneratedFile;
import com.payflow.soa_modernization_service.model.GeneratedProject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ProjectWriterService {

    private static final String OUTPUT_DIR = "../generated-projects";

    private static final DateTimeFormatter OUTPUT_TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public String writeProject(GeneratedProject project) {

        try {
            Path projectRoot = Path.of(
                    OUTPUT_DIR,
                    project.getProjectName()
                            + "_"
                            + LocalDateTime.now()
                            .format(OUTPUT_TIMESTAMP_FORMAT));

            Files.createDirectories(projectRoot);

            for (GeneratedFile file : project.getFiles()) {

                Path targetFile =
                        projectRoot.resolve(file.getPath());

                Path parentDir =
                        targetFile.getParent();

                if (parentDir != null) {
                    Files.createDirectories(parentDir);
                }

                Files.writeString(
                        targetFile,
                        file.getContent());
            }

            return projectRoot.toString();

        } catch (IOException ex) {
            throw new RuntimeException(
                    "Failed to write generated project",
                    ex);
        }
    }
}
