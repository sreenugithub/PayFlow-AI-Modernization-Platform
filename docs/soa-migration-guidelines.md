# SOA to Spring Boot Migration Guidelines

## Goal
Convert legacy SOA artifacts into a modern Spring Boot microservice using staged agent-based migration.

## SOA to Spring Boot Mapping

| SOA Artifact | Spring Boot Equivalent |
|---|---|
| composite.xml | Service/module summary |
| BPEL process | Service orchestration logic |
| BPEL receive | REST controller endpoint |
| BPEL assign | Business validation/mapping logic |
| BPEL invoke | WebClient/Feign downstream client |
| BPEL reply | API response |
| WSDL operation | Controller method |
| XSD element | DTO class |
| Partner link/reference | External service client |
| Fault policy | Exception handler |

## Stage 1: Analyze SOA Project
Agent must read:
- composite.xml
- BPEL files
- WSDL files
- XSD files

Output:
- Business summary
- Process flow
- Downstream dependencies
- Migration plan

## Stage 2: Generate Spring Boot Structure
Agent must generate:
- controller
- service
- dto
- client
- exception
- config
- README

## Stage 3: Convert Contract
Agent converts:
- XSD elements to DTOs
- WSDL operations to REST endpoints

## Stage 4: Convert Business Logic
Agent converts:
- receive to controller
- assign to validation/mapping
- invoke to client call
- reply to response object

## Stage 5: Generate Tests
Agent creates:
- unit tests
- controller tests
- sample request/response

## Stage 6: Review and Fix
Agent checks:
- compilation issues
- missing imports
- incorrect package names
- missing exception handling

## Stage 7: Deployment
Agent generates:
- Dockerfile
- README
- deployment notes