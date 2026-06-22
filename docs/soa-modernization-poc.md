# SOA to Spring Boot Modernization POC

## Problem Statement

Legacy SOA applications are difficult to maintain, test, deploy, and scale using modern cloud-native practices. SOA artifacts such as `composite.xml`, BPEL, WSDL, and XSD describe business flows and contracts, but they are not directly runnable as Spring Boot microservices.

This POC demonstrates an AI-assisted migration approach that reads a legacy SOA composite and generates a runnable Spring Boot Gradle service.

The use case implemented in this POC is:

```text
PaymentValidationComposite -> payment-validation-service
```

The modernization uses three main areas in this repository:

| Area | Purpose |
|---|---|
| `soa-modernization-lab` | Source SOA artifacts used as migration input |
| `soa-modernization-service` | AI-assisted migration engine |
| `generated-projects/payment-validation-service` | Generated Spring Boot output service |

## Architecture

### Source Layer

`soa-modernization-lab/PaymentValidationComposite` contains the legacy SOA project:

- `composite.xml`
- `bpel/PaymentValidation.bpel`
- `wsdl/PaymentValidation.wsdl`
- `xsd/PaymentValidation.xsd`

The SOA composite defines:

- `PaymentValidationService`
- `AccountValidationService` reference
- `PaymentValidationBPEL` component

### Modernization Layer

`soa-modernization-service` performs the migration orchestration.

Key classes:

| Class | Responsibility |
|---|---|
| `SoaModernizationController` | Exposes migration API |
| `SoaModernizationService` | Coordinates read, prompt, AI call, JSON parsing, and writing |
| `SoaProjectReader` | Reads SOA XML, BPEL, WSDL, XSD, and migration rules |
| `SoaMigrationPromptBuilder` | Builds the AI migration prompt |
| `OpenAIService` | Calls OpenAI Responses API |
| `ProjectWriterService` | Writes generated files into `generated-projects` |

### Generated Layer

`generated-projects/payment-validation-service` is the generated Spring Boot microservice.

Generated structure includes:

- Controller package
- Service package
- DTO package
- Client package
- Exception package
- Gradle build
- Dockerfile
- README

## Flow Diagram

```text
Developer / User
      |
      | POST /api/soa/migrate
      v
soa-modernization-service
      |
      | readProject(soaProjectPath)
      v
soa-modernization-lab/PaymentValidationComposite
      |
      | composite.xml + BPEL + WSDL + XSD + migration rules
      v
SoaMigrationPromptBuilder
      |
      | huge AI prompt
      v
OpenAIService
      |
      | OpenAI JSON response
      v
ObjectMapper -> GeneratedProject
      |
      | writeProject(project)
      v
generated-projects/payment-validation-service
      |
      | ./gradlew clean build
      v
Runnable Spring Boot Service
```

## Input

### Migration API

```http
POST http://localhost:8087/api/soa/migrate
```

Request body:

```json
{
  "soaProjectPath": "soa-modernization-lab/PaymentValidationComposite"
}
```

### SOA Input Files

#### composite.xml

```xml
<composite name="PaymentValidationComposite">
    <service name="PaymentValidationService"/>
    <reference name="AccountValidationService"/>
    <component name="PaymentValidationBPEL"/>
</composite>
```

#### BPEL

```xml
<process name="PaymentValidationProcess">
    <receive name="ReceivePaymentRequest"/>
    <assign name="ValidatePayment"/>
    <invoke name="AccountValidationService"/>
    <reply name="SendResponse"/>
</process>
```

#### WSDL

The WSDL defines the `validatePayment` operation and request/response contract.

#### XSD

The XSD defines the payment validation request fields:

- `paymentReference`
- `paymentType`
- `amount`

### AI Prompt Input

`SoaMigrationPromptBuilder` merges all SOA inputs into one large prompt containing:

- SOA composite XML
- BPEL content
- WSDL content
- XSD content
- migration guidelines from `docs/soa-migration-guidelines.md`

The prompt instructs AI to return only JSON in this structure:

```json
{
  "projectName": "",
  "description": "",
  "files": [
    {
      "path": "",
      "content": ""
    }
  ]
}
```

## Output

### Migration API Response

`soa-modernization-service` returns:

```json
{
  "projectName": "payment-validation-service",
  "outputPath": "../generated-projects/payment-validation-service_<timestamp>",
  "filesGenerated": 10,
  "status": "SUCCESS"
}
```

### Generated Spring Boot Service

The generated service is available at:

```text
generated-projects/payment-validation-service
```

Main generated files:

| File | Purpose |
|---|---|
| `PaymentValidationApplication.java` | Spring Boot main class |
| `PaymentValidationController.java` | REST endpoint |
| `PaymentValidationService.java` | Business validation logic |
| `PaymentValidationRequestDto.java` | Request DTO |
| `PaymentValidationResponseDto.java` | Response DTO |
| `AccountValidationClient.java` | Downstream client for account validation |
| `GlobalExceptionHandler.java` | API exception handling |
| `Dockerfile` | Container build |
| `README.md` | Service documentation |

### Generated Service API

```http
POST /api/payments/validate
```

Request:

```json
{
  "paymentReference": "PAY-123",
  "paymentType": "CARD",
  "amount": 100.50
}
```

Response:

```json
{
  "paymentReference": "PAY-123",
  "valid": true,
  "message": "Payment validated successfully",
  "accountStatus": "ACTIVE"
}
```

## Results

The POC successfully demonstrates:

1. Reading SOA artifacts from `soa-modernization-lab`.
2. Building one combined AI migration prompt.
3. Calling OpenAI through `OpenAIService`.
4. Parsing AI output into a `GeneratedProject`.
5. Writing generated files into `generated-projects`.
6. Building the generated Spring Boot service with Gradle.
7. Producing a runnable `payment-validation-service`.

Verified build command:

```bash
cd generated-projects/payment-validation-service
./gradlew clean build
```

Result:

```text
BUILD SUCCESSFUL
```

## Benefits

- Reduces manual analysis effort for SOA migration.
- Converts SOA structure into Spring Boot project structure.
- Preserves SOA intent by mapping BPEL and WSDL concepts into modern REST/service/client patterns.
- Produces developer-readable code instead of only documentation.
- Creates a repeatable migration pipeline for other SOA composites.
- Generates a Gradle-based service that can be built, tested, and containerized.

## Future Enhancements

1. Add automated validation for generated JSON before writing files.
2. Add generated unit tests and controller tests for every migrated service.
3. Add compile-fix loop to automatically repair generated code.
4. Add UI screen to upload/select SOA projects and trigger migration.
5. Add migration status tracking and generated project history.
6. Add support for multiple BPEL, WSDL, and XSD files.
7. Add service dependency mapping for SOA partner links.
8. Add API gateway route generation for generated services.
9. Add Docker Compose generation for local testing.
10. Add human review workflow before committing generated code.

