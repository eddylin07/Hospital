# Repository Guidance

## Project

- Name: Hospital
- Stack: Spring Boot 2.0, MyBatis, Freemarker, Maven, Java 8-era dependencies.
- Purpose: medical information management system.

## Development Notes

- Keep changes small and aligned with existing controller/service/mapper patterns.
- Prefer focused JUnit 4 tests with Mockito for service/controller logic.
- Mapper XML behavior can be tested by parsing the XML or MyBatis mapped statements when a database is not required.
- Do not add fragile UI/snapshot tests for cosmetic template/static-resource changes.

## Validation

- Use Maven for tests.
- Lombok 1.16.22 is not compatible with modern javac module defaults; prefer JDK 8 when available:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh environment, install them before validation.
