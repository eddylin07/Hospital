# Project Agent Instructions

## Project

- Name: Hospital
- Updated: 2026-08-13
- Stack: Java Spring Boot 2, MyBatis, Maven, JSP/Thymeleaf-style web resources.

## Development Notes

- Follow existing Spring/MyBatis package structure under `src/main/java/com/hospital`.
- Keep behavior changes small and focused; prefer tests that document business invariants.
- Use JUnit 4 and Mockito-style tests when adding coverage.

## Validation

- This project is known to require JDK 8 because its Lombok version is incompatible with newer javac module access.
- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- If Maven or JDK 8 is missing in a fresh environment, install `maven` and `openjdk-8-jdk-headless` before validation.
