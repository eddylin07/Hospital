# AGENTS.md

## Project

- Name: Hospital
- Type: Spring Boot 2.0.0 / MyBatis / Maven web application with FreeMarker templates.
- Package root: `com.hospital`

## Development notes

- Prefer small, focused changes that preserve existing controller/service/mapper conventions.
- Keep tests deterministic and independent. Use JUnit 4/Mockito style from `spring-boot-starter-test` when adding tests.
- The repository may need JDK 8 for Maven test runs because Lombok 1.16.22 is not compatible with newer javac module restrictions.

## Validation

- Preferred full test command:
  - `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh cloud image, install them before validating.
