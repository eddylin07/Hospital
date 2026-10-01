# AGENTS.md

## Project

- Name: Hospital
- Type: Spring Boot 2.0 / MyBatis medical information management system
- Primary language: Java
- Build tool: Maven

## Environment

- Prefer JDK 8 for local validation. The checked-in Lombok 1.16.22 dependency is not compatible with newer javac module access defaults.
- Run tests with:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

## Testing guidance

- Follow existing Maven/JUnit conventions.
- Keep tests deterministic and independent of a running database unless a mapper XML parser test is enough to validate SQL shape.
- Prioritize regression tests for authentication/session behavior, MyBatis SQL, validation, billing, and inventory paths.

## Notes

- This file was created on 2026-10-01 because no project-local `AGENTS.md` existed and no template was available under `~/.codex/templates/`.
