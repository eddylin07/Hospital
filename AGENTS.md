# AGENTS.md

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0, Maven, MyBatis, FreeMarker, MySQL

## Build and test

- Use JDK 8 for Maven commands because Lombok 1.16.22 is not compatible with modern javac module access.
- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- If Maven or JDK 8 is unavailable in a fresh environment, install `maven` and `openjdk-8-jdk-headless`.

## Working guidelines

- Follow the existing Maven/Spring/MyBatis layout under `src/main` and `src/test`.
- Keep regression tests deterministic and focused on meaningful business behavior.
- Do not store credential values in documentation or tests.
