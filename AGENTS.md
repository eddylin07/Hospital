# Repository Instructions

Project: Hospital
Date initialized: 2026-08-06

## Stack

- Java Spring Boot 2.0.0.RELEASE application.
- MyBatis mapper XML files under `src/main/resources/mapper`.
- Freemarker templates under `src/main/resources/templates`.
- Maven build via `pom.xml`.

## Working Guidelines

- Follow existing Spring/MyBatis/JUnit conventions.
- Keep tests deterministic and focused on business behavior.
- Prefer narrow testability refactors over broad production changes.
- Use Java 8 for Maven validation when available:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Notes

- The expected template directory `/home/ubuntu/.codex/templates` was not present when this file was created, so this project-local file captures inferred guidance.
