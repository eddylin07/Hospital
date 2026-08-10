# Repository Instructions

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven, MySQL
- Domain: medical information management system
- Last updated: 2026-08-10

## Working Guidelines

- Prefer small, focused changes that match existing Spring MVC/MyBatis patterns.
- For security and data-integrity work, trace the full controller-service-mapper path before changing behavior.
- Do not record credential values in documentation or memory. If credentials are relevant, record only the file path where they are configured.
- Run focused tests for changed behavior when possible. Use `mvn test` for repository-level validation when dependencies are available.

## Known Environment Notes

- This repository may require Maven to be installed in the cloud image before running tests.
- Java 21 can expose compatibility issues in older Lombok, Maven Surefire, and Mockito/ByteBuddy versions.
