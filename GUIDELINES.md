# Music Platform Microservices Guidelines

This document outlines the mandatory technical standards and best practices for all microservices in the Music Platform project.

## 1. API Response Standard (`ApiResponse<T>`)
All endpoints must return a consistent JSON structure using the `ApiResponse` wrapper.

```json
{
  "status": "SUCCESS | ERROR | WARNING | INFO",
  "message": "User-friendly message in English",
  "data": { ... } | [ ... ] | null,
  "logUser": "Username or ANONYMOUS",
  "logMessage": "Technical detail for debugging",
  "logDate": "ISO-8601 Timestamp"
}
```

## 2. Error Handling
- Use `@RestControllerAdvice` for global exception handling.
- Capture specific exceptions (e.g., `BadCredentialsException`) and map them to appropriate HTTP status codes while maintaining the `ApiResponse` body.
- Always log the technical error in `logMessage` for internal audit.

## 3. Database & Entity Naming
- **Tables**: Use `snake_case` with a `tbl_` prefix (e.g., `tbl_user`).
- **Columns**: Use `snake_case`.
- **Primary Keys**: Mandatory use of `UUID` for all main entities to ensure global uniqueness and prevent ID enumeration attacks.
- **Java Entities**: Use `CamelCase`.

## 4. Documentation (OpenAPI/Swagger)
- Every microservice must include `springdoc-openapi`.
- Configuration class `OpenApiConfig.java` must define:
    - API Title and Version.
    - Security Scheme (JWT Bearer Auth).
- Use `@Operation` and `@Tag` annotations in Controllers to document endpoint functionality.
- Swagger UI must be accessible at `/swagger-ui/index.html`.

## 5. Security & Token Validation
- **Stateless Authentication**: All services must use JWT (JSON Web Tokens) for authentication and session management.
- **Route Protection**: Token validation and signature verification are **mandatory only for private or administrator routes**.
- **Public Routes**: Endpoints intended for public access (e.g., public catalogs, health checks, login) do not require token validation.
- **Signature Validation**: For protected routes, non-authentication microservices (Resource Servers) must validate the incoming token's signature using the shared secret.
- **Shared Secret (Development)**: For local development, the following secret is used: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`.
- **Production Environment**: In production, the `jwt.secret` MUST be injected via environment variables.

## 6. Language Policy
- **Code**: All variables, classes, and methods must be in English.
- **Comments**: All code comments must be in English.
- **Messages**: All user-facing and log messages must be in English.

## 7. Audit Fields
All responses and key entities should track:
- Who performed the action (`logUser`).
- When the action occurred (`logDate`).
```
