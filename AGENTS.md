# Agent Instructions

## Project

- Project name: Hospital
- Stack: Java, Spring Boot 2.0, Maven, MyBatis, FreeMarker, MySQL.
- Primary source code: `src/main/java/com/hospital`.
- MyBatis mapper XML files: `src/main/resources/mapper`.
- There may be no existing tests on fresh branches; add tests under `src/test/java`.

## Development guidelines

- Keep changes narrowly scoped to the requested behavior.
- Prefer existing service, controller, mapper, and utility patterns.
- Do not change production behavior when adding coverage unless a small correctness or testability fix is required by the test.
- Use deterministic unit tests with JUnit 4 and Mockito patterns available through `spring-boot-starter-test`.
- Avoid tests that require a live database unless the task explicitly needs integration coverage.

## Validation

- Lombok 1.16.22 is incompatible with Java 21 in this project.
- Use JDK 8 for Maven validation:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If Maven or JDK 8 is missing in a fresh environment, install `maven` and `openjdk-8-jdk-headless` before validation.
