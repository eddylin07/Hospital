# MEMORY.md

## Project facts

- Project name: Hospital
- Stack: Spring Boot 2.0, MyBatis, FreeMarker, Maven, JUnit via `spring-boot-starter-test`.
- Source layout: production Java under `src/main/java/com/hospital`, MyBatis XML under `src/main/resources/mapper`, templates under `src/main/resources/templates`.

## Validation notes

- Use JDK 8 for tests because Lombok 1.16.22 can fail on newer javac module boundaries.
- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Coverage automation notes

- The template directory `/home/ubuntu/.codex/templates` was unavailable in this environment, so AGENTS.md and MEMORY.md were created as project-local minimal equivalents.
- Existing repository checkout initially has no `src/test` tree on this branch.
