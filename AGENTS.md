# AGENTS.md

## Project

- Name: Hospital
- Stack: Spring Boot 2.0, MyBatis, Freemarker, Maven, JUnit 4/Mockito tests.
- Purpose: medical information management system.

## Local workflow

- Use Java 8 for Maven commands because Lombok 1.16.22 is not compatible with modern javac module boundaries.
- Preferred validation command:
  - `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- Follow existing test style under `src/test/java` when present. Keep tests deterministic and avoid database/network dependencies unless a test fixture explicitly provides them.

## Coding guidance

- Keep changes scoped to the behavior under test.
- Prefer mapper XML parsing or service/controller unit tests with mocks over full Spring context tests for narrow regression coverage.
- Do not store credentials in repository files; document only credential locations if needed.
