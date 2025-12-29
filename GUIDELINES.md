# Music Platform Microservices Guidelines

This document outlines the mandatory technical standards and best practices for all microservices in the Music Platform project.

## 1. Architecture: Layered Pattern
All microservices follow a consistent layered architecture to ensure separation of concerns:
- **Controller Layer**: Handles HTTP requests, input validation, and maps DTOs.
- **Service Layer**: Contains business logic and orchestrates calls between repositories and other services.
- **Repository Layer**: Interfaces with the database using Spring Data JPA.
- **Entity Layer**: Represents the database schema.
- **DTO Layer**: Data Transfer Objects for API requests and responses.

## 2. Naming Conventions
To maintain clarity across the codebase, classes must include their role in their name:
- **Entities**: Must end in `Entity` (e.g., `UserEntity`).
- **Enums**: Must end in `Enum` (e.g., `UserRoleEnum`).
- **Repositories**: Must end in `Repository` (e.g., `UserRepository`).
- **Services**: Must end in `Service` (e.g., `AuthService`).
- **Controllers**: Must end in `Controller` (e.g., `AuthController`).
- **DTOs**: Must end in `Request` or `Response` (e.g., `RegisterRequest`, `UserResponse`).

## 3. Audit Fields
All main entities must implement auditing to track record lifecycle. The standard field names are:
- `createdBy`: (String) The user who created the record.
- `createdAt`: (LocalDateTime) The timestamp of creation.
- `updatedBy`: (String) The last user who modified the record.
- `updatedAt`: (LocalDateTime) The timestamp of the last modification.

Use Spring Data JPA `@CreatedBy`, `@CreatedDate`, `@LastModifiedBy`, and `@LastModifiedDate` annotations.

## 4. API Response Standard (`ApiResponse<T>`)
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

## 5. Error Handling
- Use `@RestControllerAdvice` for global exception handling.
- Capture specific exceptions (e.g., `BadCredentialsException`) and map them to appropriate HTTP status codes while maintaining the `ApiResponse` body.
- Always log the technical error in `logMessage` for internal audit.

## 6. Database & Entity Naming
- **Tables**: Use `snake_case` with a `tbl_` prefix (e.g., `tbl_user`).
- **Columns**: Use `snake_case`.
- **Primary Keys**: Mandatory use of `UUID` for all main entities to ensure global uniqueness and prevent ID enumeration attacks.

## 7. Documentation (OpenAPI/Swagger)
- Every microservice must include `springdoc-openapi`.
- Configuration class `OpenApiConfig.java` must define:
    - API Title and Version.
    - Security Scheme (JWT Bearer Auth).
- Use `@Operation` and `@Tag` annotations in Controllers to document endpoint functionality.
- Swagger UI must be accessible at `/swagger-ui/index.html`.

## 8. Security & Token Validation
- **Stateless Authentication**: All services must use JWT (JSON Web Tokens) for authentication and session management.
- **Route Protection**: Token validation and signature verification are **mandatory only for private or administrator routes**.
- **Public Routes**: Endpoints intended for public access (e.g., public catalogs, health checks, login) do not require token validation.
- **Signature Validation**: For protected routes, non-authentication microservices (Resource Servers) must validate the incoming token's signature using the shared secret.
- **Shared Secret (Development)**: For local development, the following secret is used: `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`.
- **Production Environment**: In production, the `jwt.secret` MUST be injected via environment variables.

## 9. Language Policy
- **Code**: All variables, classes, and methods must be in English.
- **Comments**: All code comments must be in English.
- **Messages**: All user-facing and log messages must be in English.
```
