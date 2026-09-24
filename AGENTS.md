# Agent Instructions

## Project

- Project name: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven.
- Main source: `src/main/java`
- Mapper XML: `src/main/resources/mapping`
- Tests: `src/test/java`

## Build and test

- Lombok 1.16.22 requires JDK 8 in this environment.
- Prefer running tests with:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If Maven or JDK 8 is missing in a fresh environment, install `maven` and `openjdk-8-jdk-headless`.

## Testing guidance

- Follow existing JUnit 4 / Mockito style for unit tests.
- Keep tests deterministic and independent of a live database.
- For mapper XML invariants, parse XML directly when possible instead of requiring MySQL.
- Focus coverage on business-risk behavior: login/session state, authorization redirects, prescription/dispensing inventory and price updates, mapper SQL constraints, and date formatting.
