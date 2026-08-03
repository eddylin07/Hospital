# Agent Instructions

## Project

- Name: Hospital
- Type: Spring Boot 2 hospital management application
- Stack: Java, Maven, MyBatis, Freemarker, JUnit 4/Mockito tests

## Working Rules

- Follow existing Spring service, controller, mapper XML, and utility patterns.
- Keep changes focused on regression prevention and business behavior.
- Prefer deterministic unit or mapper XML parsing tests over environment-dependent integration tests.
- Use JDK 8 for Maven validation when available:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- Do not store credentials or secret values in repository files.

## Test Coverage Automation Focus

- Prioritize recently changed production code, edge-case validation, SQL mapper behavior, login/session flows, and shared parsing utilities.
- Avoid low-signal snapshot or cosmetic template tests.
- Add the minimum tests that prove the invariant and document the user-facing risk covered.
