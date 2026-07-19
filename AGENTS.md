# AGENTS.md

## Project

- Name: Hospital
- Type: Spring Boot 2.0.0.RELEASE / MyBatis / Maven application
- Purpose: Medical information management system

## Working rules

- Read `MEMORY.md` before making code or test changes.
- Keep changes scoped to the requested task.
- Follow existing Java, Maven, and test conventions.
- Prefer focused regression tests for risky business behavior over broad low-signal snapshots.
- Use JDK 8 for Maven validation because the project uses Lombok 1.16.22.

## Validation

- Primary test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is unavailable, install/remediate the local environment before validating.
