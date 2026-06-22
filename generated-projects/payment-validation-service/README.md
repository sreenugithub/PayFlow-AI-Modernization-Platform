# Payment Validation Service

Spring Boot 3.x migration of the legacy SOA composite `PaymentValidationComposite`.

## What it does
- Exposes a REST endpoint for `validatePayment`
- Validates the incoming payment request
- Invokes an external Account Validation service
- Returns a payment validation response

## SOA to Spring Boot mapping
- `composite.xml` -> service/module summary
- `BPEL receive` -> REST controller endpoint
- `BPEL assign` -> service validation logic
- `BPEL invoke` -> downstream client call
- `BPEL reply` -> response DTO
- `WSDL operation validatePayment` -> POST `/api/payments/validate`
- `XSD PaymentValidationRequest` -> `PaymentValidationRequestDto`

## Tech stack
- Java 17
- Spring Boot 3.x
- Gradle
- Web, Validation, WebFlux, Actuator

## Run locally
```bash
./gradlew bootRun
```

## Build
```bash
./gradlew clean build
```

## API
### POST /api/payments/validate
Request:
```json
{
  "paymentReference": "PAY-123",
  "paymentType": "CARD",
  "amount": 100.50
}
```

Success response:
```json
{
  "paymentReference": "PAY-123",
  "valid": true,
  "message": "Payment validated successfully",
  "accountStatus": "ACTIVE"
}
```

## Configuration
External account validation service URL:
```yaml
external:
  account-validation:
    base-url: http://localhost:8081
```

## Docker
```bash
docker build -t payment-validation-service .
docker run -p 8080:8080 payment-validation-service
```
