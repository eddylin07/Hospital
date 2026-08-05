# Agent Guide

## Project

- Name: Hospital
- Type: Spring Boot 2.0.0.RELEASE / MyBatis / Maven web application.
- Java compatibility: use JDK 8 for builds and tests because Lombok 1.16.22 is not compatible with modern javac module access.

## Development

- Keep production behavior changes minimal and scoped to the tested risk.
- Follow existing package layout under `src/main/java/com/hospital` and `src/test/java/com/hospital`.
- Prefer deterministic unit tests and mapper XML parsing tests over environment-dependent integration tests unless a database is explicitly available.

## Testing

- Preferred validation command:
  - `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh environment, install `maven` and `openjdk-8-jdk-headless` before running the test target.
