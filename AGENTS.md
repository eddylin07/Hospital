# AGENTS.md

## Project

- Name: HospitalAction
- Root: `/workspace`
- Stack: Java, Maven, Spring Boot 2.0, MyBatis, Freemarker

## Working rules

- Follow existing source and test conventions.
- Prefer focused, deterministic tests for business-risk behavior.
- Keep production changes minimal; only refactor for testability when necessary.
- Use Java 8 for validation because Lombok 1.16.22 is not compatible with newer javac module access.

## Validation

- Preferred test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh environment, install them before validation.
