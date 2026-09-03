# Repository Instructions

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Layui, MySQL, Maven
- Purpose: medical information management system.

## Working Guidelines

- Keep changes minimal and focused on the requested behavior.
- Prefer existing Spring MVC, service, mapper, and FreeMarker patterns over introducing new frameworks.
- Treat authentication, authorization, patient data ownership, inventory updates, and PDF/export flows as high-risk paths.
- Do not commit secrets or local environment-specific values.

## Testing

- Use Maven tests when changing Java code.
- If Maven is unavailable in the cloud image, install it before running tests.
- For narrow service-level regression tests, simple fake implementations are preferred over Mockito when Java 21 compatibility is a concern.
