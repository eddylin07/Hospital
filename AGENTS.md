# Repository Instructions

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, Maven
- Main validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`

## Working Rules

- Follow existing package names and test conventions under `src/test/java`.
- Prefer focused regression tests for business logic, mapper SQL, parsing, validation, permissions, and shared utilities.
- Keep tests deterministic and independent; avoid cosmetic snapshot-style tests.
- Do not change production behavior unless a small fix is required to make the newly covered invariant true.

## Environment Notes

- The project uses Lombok 1.16.22, which requires JDK 8 on this runner.
- Some fresh images may need Maven and JDK 8 installed before tests can run.
