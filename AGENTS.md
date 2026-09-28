# Agent Guidelines

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven
- Purpose: Medical information management system.

## Development

- Follow existing Java package structure under `src/main/java/com/hospital`.
- Keep tests under `src/test/java` and prefer deterministic unit tests for utilities and service logic.
- Use Maven for build and test commands.

## Validation

- Run the most relevant Maven test target for changed areas.
- If a full test suite is practical, run `mvn test`.
