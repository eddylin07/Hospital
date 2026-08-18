# Hospital Repository Instructions

## Project

- Java Spring Boot 2.0 application using MyBatis mapper XML, Freemarker templates, and Maven.
- Source code lives under `src/main/java`; MyBatis XML mappers live under `src/main/resources/mapper`.
- Tests should follow JUnit 4 / Spring Boot test conventions available from `spring-boot-starter-test`.

## Development Rules

- Keep production behavior changes minimal and scoped to the tested invariant.
- Prefer focused unit or XML parsing tests for service logic and mapper SQL regressions.
- Do not require a live MySQL database for regression tests unless the change explicitly needs integration coverage.
- Use deterministic tests with local mocks or parsed mapper XML.

## Validation

- Lombok 1.16.22 is incompatible with the default Java 21 compiler in this environment.
- Run Maven tests with JDK 8:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If Maven or JDK 8 is missing, install `maven` and `openjdk-8-jdk-headless` before validating.
