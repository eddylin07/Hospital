# AGENTS.md

## Project

- Name: Hospital
- Date initialized: 2026-10-08
- Stack inferred from repository: Java, Maven, Spring Boot 2.0.0, MyBatis, Freemarker, JUnit 4/Spring Boot test.

## Working Guidelines

- Keep production changes minimal and behavior-focused.
- Follow existing package layout under `com.hospital`.
- Prefer deterministic unit or mapper XML tests for regression coverage.
- Use existing Maven conventions; place tests under `src/test/java`.
- This project may require Java 8 for Maven test runs because the checked-in Lombok version is old.

## Validation

- Run the smallest relevant Maven test target for the touched code.
- When feasible, run the full test suite with `mvn test`.
