# Project Agent Instructions

## Project

- Name: Hospital
- Type: Java web application
- Stack: Maven, Spring Boot 2.0.0, MyBatis, FreeMarker, MySQL
- Date initialized: 2026-09-06

## Development rules

- Follow existing package structure under `src/main/java/com/hospital`.
- Keep changes focused on the behavior under test; avoid unrelated refactors.
- Prefer deterministic unit tests over database-dependent integration tests unless the mapper XML itself is the behavior being checked.
- Use JUnit 4 conventions from `spring-boot-starter-test` for tests.

## Validation

- Use JDK 8 for Maven commands because this project uses Lombok 1.16.22.
- Preferred test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- If Maven or JDK 8 is missing in a fresh environment, install Maven and `openjdk-8-jdk-headless` before validation.
