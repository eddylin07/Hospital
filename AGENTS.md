# Agent Instructions

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0, Maven, MyBatis, FreeMarker, MySQL
- Date initialized: 2026-09-05

## Development notes

- Use the existing Maven/JUnit 4/Spring Boot test conventions.
- Keep tests deterministic and independent; prefer small unit tests or XML parsing assertions for mapper behavior when a database is not required.
- The project uses an older Lombok version. If the default JDK is too new, run Maven with JDK 8.

## Validation

- Preferred command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh environment, install them before validation.

## Missing template context

- `/home/ubuntu/.codex/templates/AGENTS.md` was not present when this file was initialized, so this file captures the inferred project-specific rules.
