# AGENTS.md

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, Maven
- Purpose: Hospital / medical information management system.

## Working conventions

- Follow existing package layout under `src/main/java/com/hospital`.
- Keep production behavior changes minimal and scoped to the tested risk.
- Prefer focused JUnit 4 tests that match the existing Spring Boot test dependency stack.
- Avoid brittle UI snapshot tests; prioritize service logic, mapper XML invariants, parsing, validation, and authentication/session behavior.

## Validation

- Lombok 1.16.22 is incompatible with the default Java 21 compiler in this environment.
- Use JDK 8 for Maven validation:
  - `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 are missing in a fresh image, install `maven` and `openjdk-8-jdk-headless` before running tests.

