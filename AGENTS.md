# AGENTS.md

## Project

- Name: Hospital
- Type: Spring Boot 2.0.0.RELEASE web application
- Build: Maven
- Main stack: Java, MyBatis, FreeMarker, MySQL, JUnit 4 via `spring-boot-starter-test`

## Working rules

- Follow existing package structure under `com.hospital`.
- Prefer focused unit tests and XML parser tests over broad Spring context tests unless integration behavior requires it.
- Keep tests deterministic and independent of a live MySQL database.
- Use Java 8 for Maven validation because the checked-in Lombok version is not compatible with newer JDK module boundaries.
- Do not store credentials in repository files.

## Useful commands

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```
