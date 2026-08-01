# Project Agent Instructions

Project: Hospital
Date initialized: 2026-08-01

## Stack

- Java Maven project (`pom.xml`)
- Spring Boot 2.0.0.RELEASE
- MyBatis / PageHelper
- Freemarker views
- MySQL connector
- JUnit 4 style tests via `spring-boot-starter-test`

## Working rules

- Keep changes focused on the requested behavior.
- Follow existing package structure under `src/main/java/com/hospital`.
- Put tests under the matching package path in `src/test/java`.
- Prefer deterministic unit tests with mocks or XML parsing over database-dependent tests.
- Do not commit credentials or machine-local secrets.
- When validating on modern cloud runners, this project may require JDK 8 because Lombok 1.16.22 is incompatible with recent javac module restrictions.
