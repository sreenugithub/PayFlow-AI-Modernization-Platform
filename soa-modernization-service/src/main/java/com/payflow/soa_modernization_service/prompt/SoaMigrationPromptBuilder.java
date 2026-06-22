package com.payflow.soa_modernization_service.prompt;

import com.payflow.soa_modernization_service.reader.SoaProjectData;
import org.springframework.stereotype.Component;

@Component
public class SoaMigrationPromptBuilder {

    public String buildPrompt(SoaProjectData data) {
        return """
You are a Senior Java Architect.

Convert the following SOA application into a complete runnable Spring Boot Gradle project.

Requirements:

1. Java 17
2. Spring Boot 3.x
3. Gradle
4. Controller package
5. Service package
6. DTO package
7. Client package
8. Exception package
9. Dockerfile
10. README

Return ONLY JSON.

Format:

{
  "projectName":"",
  "description":"",
  "files":[
    {
      "path":"",
      "content":""
    }
  ]
}

SOA COMPOSITE:
%s

BPEL:
%s

WSDL:
%s

XSD:
%s

MIGRATION RULES:
%s
""".formatted(
                data.getCompositeXml(),
                data.getBpelContent(),
                data.getWsdlContent(),
                data.getXsdContent(),
                data.getMigrationGuidelines()
        );
    }
}
