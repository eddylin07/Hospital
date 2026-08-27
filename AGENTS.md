# AGENTS.md

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven, MySQL
- Purpose: Medical information management system.

## Working rules

- Follow existing Spring MVC service/mapper patterns and keep fixes narrowly scoped.
- Prefer focused unit tests for security, data integrity, and crash fixes.
- On Java 21, Maven test compatibility may require updated Lombok/Surefire versions.
- Do not store secrets in repository files; document only secret locations when needed.

## Validation

- Run `mvn test` after code or test changes when Maven is available.
- If Maven is missing in the cloud image, install it before running tests.
