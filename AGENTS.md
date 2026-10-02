# AGENTS.md

## Project

- Name: Hospital
- Generated on: 2026-10-02
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven, JUnit 4 via `spring-boot-starter-test`

## Working Rules

- Read `MEMORY.md` before changing code and update it when learning durable project facts.
- Keep changes scoped to the requested behavior; avoid unrelated refactors.
- Follow existing Maven/Spring/MyBatis layout under `src/main`.
- Prefer deterministic unit or mapper XML tests for regression coverage.

## Test Commands

- This project uses Lombok 1.16.22, so run tests with JDK 8:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If Maven or JDK 8 is missing in a fresh environment, install them before validation.
