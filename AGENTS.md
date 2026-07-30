# AGENTS.md

## Project

- Name: Hospital
- Type: Java Spring Boot hospital management application
- Stack: Maven, Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, JUnit 4, Mockito

## Working rules

- Keep changes scoped to the requested behavior or tests.
- Follow existing package structure under `com.hospital`.
- Prefer deterministic unit tests over full application-context tests unless integration coverage is required.
- Use Java 8 for builds and tests; Lombok 1.16.22 is not compatible with modern javac module defaults.

## Validation

- Primary test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh environment, install Maven and `openjdk-8-jdk-headless`.

## Notes for automation

- Read `MEMORY.md` before changing production or test code.
- Record durable project learnings in `MEMORY.md` before finishing a run.
