# Agent Instructions

## Project

- Project name: HospitalAction
- Repository root: `/workspace`
- Stack: Java, Spring Boot 2.0.0.RELEASE, MyBatis, Maven, JUnit/Mockito tests.

## Working Rules

- Read `MEMORY.md` before changing code.
- Keep changes scoped to the requested behavior and follow existing Maven/Spring conventions.
- Use Java 8 for compilation and tests because the project depends on Lombok 1.16.22.
- Prefer focused unit or XML-mapper tests for regression coverage. Avoid broad integration tests that require a live database unless explicitly needed.

## Validation

- Preferred test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing on a fresh runner, install them before validating.
