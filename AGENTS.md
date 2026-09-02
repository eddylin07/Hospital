# Project Agent Instructions

## Project

- Name: Hospital
- Type: Java Spring Boot 2 / MyBatis medical information management system
- Build tool: Maven
- Primary language: Java

## Working Rules

- Follow the repository's existing Maven/Spring/MyBatis conventions.
- Prefer focused JUnit 4 tests under `src/test/java` for regression coverage.
- Use deterministic unit tests or mapper XML parsing tests when database-backed integration tests are not required.
- Keep production changes minimal and limited to behavior required by tests.
- Do not record credentials or secret values in this repository.

## Validation

- Use JDK 8 for Maven because the project depends on Lombok 1.16.22.
- Preferred validation command:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

## Open Questions

- No upstream template was available at `/home/ubuntu/.codex/templates` when this file was created.
