# Repository Instructions

## Project

- Name: Hospital
- Type: Spring Boot 2.0 / MyBatis medical information management system.
- Build tool: Maven.
- Primary language: Java.

## Development Notes

- Prefer focused, deterministic tests that match existing JUnit 4 / Mockito conventions.
- Keep production changes minimal; coverage automation should mainly add tests.
- This project depends on Lombok 1.16.22, which is not compatible with modern JDK module access. Use Java 8 for Maven validation when available.

## Validation

- Full test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Java 8 or Maven is missing, install Maven and OpenJDK 8 in the runner before testing.
