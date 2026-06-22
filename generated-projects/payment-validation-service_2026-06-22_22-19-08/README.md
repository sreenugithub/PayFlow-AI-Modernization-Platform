# Payment Validation Service

Spring Boot 3.x application migrated from the SOA `PaymentValidationComposite`.

## Tech Stack
- Java 17
- Spring Boot 3.x
- Gradle
- REST controller
- WebClient-based downstream client
- Bean validation
- Global exception handling

## SOA Mapping
- `receive` -> `POST /api/payments/validate`
- `assign` -> service-level validation and DTO mapping
- `invoke` -> `AccountValidationClient`
- `reply` -> `PaymentValidationResponse`
- XSD `PaymentValidationRequest` -> request DTO
- WSDL `validatePayment` -> REST endpoint method

## API
### Validate Payment
`POST /api/payments/validate`

Request:

{
  "paymentReference": "REF-123",
  "paymentType": "CARD",
  "amount": 100.0
}


Success Response:

{
  "paymentReference": "REF-123",
  "status": "APPROVED",
  "message": "Payment validated successfully",
  "accountValidated": true
}


Rejected Response:

{
  "paymentReference": "REF-123",
  "status": "REJECTED",
  "message": "Insufficient balance",
  "accountValidated": false
}


## External Dependency
The service calls an external Account Validation service at:
`{external.account-validation.base-url}/api/accounts/validate`

Default base URL:
`http://localhost:8081`

## Run Locally
bash
./gradlew bootRun


## Run Tests
bash
./gradlew test


## Build
bash
./gradlew clean bootJar


## Run Docker
bash
docker build -t payment-validation-service .
docker run -p 8080:8080 payment-validation-service


## Notes
- If the downstream account validation service is unavailable, the client returns a rejected result with a fallback reason.
- Validation errors are returned as HTTP 400.
