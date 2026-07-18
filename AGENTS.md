# Project Agent Guide

## Project

- Name: Hospital
- Type: Java web application for hospital information management
- Stack: Spring Boot 2.0.0, MyBatis, Freemarker, Maven, JUnit 4/Spring Boot test
- Default branch for this run: `cursor/missing-test-coverage-682d` based on `master`

## Working Rules

- Prefer the existing Maven/Spring/MyBatis patterns over new abstractions.
- Keep tests deterministic, focused, and independent.
- Do not change production behavior for coverage work unless a very small testability refactor is required.
- When credentials are encountered, record only their file location, not their values.
- Commit and push completed work to the designated feature branch.

## Validation

- Use Maven for validation.
- This project has historically required JDK 8 because Lombok 1.16.22 can fail under newer JDKs.
- Preferred command when JDK 8 is available:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

