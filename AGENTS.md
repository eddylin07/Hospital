# AGENTS.md

## Project

- Name: Hospital
- Type: Java web application for hospital information management
- Stack: Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL

## Working rules

- Read this file and `MEMORY.md` before changing project files.
- Prefer minimal, high-confidence fixes for critical correctness or security bugs.
- Use existing Spring MVC/MyBatis patterns already present in the repository.
- Do not store credential values in documentation; only reference credential file locations.

## Validation

- Primary automated check: `mvn test`
- The cloud image may need Maven installed before tests can run.
