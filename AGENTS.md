# AGENTS.md

## Project

- Name: Hospital
- Repository purpose: Medical information management system.
- Tech stack: Java, Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, Maven, MySQL.
- Last inferred: 2026-07-31

## Working Rules

- Follow existing package structure under `src/main/java/com/hospital` and mapper XML conventions under `src/main/resources/mapper`.
- Keep production changes minimal; prefer adding focused tests for risky behavior.
- Use deterministic unit tests and avoid depending on a live database unless the existing test pattern requires it.
- Existing Lombok version is old; validation may require Java 8 instead of the runner default JDK.

## Validation

- Preferred test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- If Maven or JDK 8 is missing in the environment, install them before validation.

