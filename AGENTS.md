# AGENTS.md

## Project

- Name: Hospital
- Type: Java Spring Boot web application
- Primary build tool: Maven (`pom.xml`)
- Date initialized: 2026-09-03

## Working Rules

- Read this file and `MEMORY.md` before modifying repository files.
- Prefer the existing Spring Boot/MyBatis structure and JUnit conventions when adding tests.
- Keep changes scoped to the requested behavior and avoid unrelated refactors.
- Do not record secret values in repository memory; record only where credentials are expected to live.

## Testing

- Use the relevant Maven test target for touched Java code.
- Prior automation memory indicates the project may require JDK 8 because Lombok 1.16.22 can fail on newer JDKs.
- Known validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
