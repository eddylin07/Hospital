# Agent Instructions

## Project

- Project name: Hospital
- Repository root: `/workspace`
- Stack: Java, Spring Boot 2.0.0, MyBatis, Freemarker, Maven
- Domain: hospital / medical information management system

## Working Rules

- Default communication language with the user: Chinese.
- Keep code and command identifiers in English.
- Follow existing Spring Boot, MyBatis mapper XML, JUnit 4, and Mockito patterns.
- Prefer focused tests for behavior with regression risk over broad snapshot-style checks.
- Do not change production behavior unless a small testability refactor or a fix required by the test target is necessary.

## Validation

- Use Java 8 for Maven validation because Lombok 1.16.22 is incompatible with the default Java 21 compiler.
- Preferred command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- If Maven or JDK 8 is missing in a fresh image, install them before validation.

## Git

- Develop on branch `cursor/missing-test-coverage-4226`.
- Commit and push changes to `origin cursor/missing-test-coverage-4226`.
- Use `git push -u origin cursor/missing-test-coverage-4226`.
