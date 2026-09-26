# MEMORY.md

## Project Snapshot
- Project name: workspace
- Date initialized: 2026-09-26
- Detected stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis.

## Test Notes
- Use focused Maven test targets for coverage additions when possible.
- Java 8 is required for this project because Lombok 1.16.22 is incompatible with newer javac module access.
- 2026-09-26: Added JUnit 4 tests for login interceptor redirect/allow behavior and `WebConfig` public-route exclusions. Validation: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Pending Unknowns
- No project-specific testing conventions were documented before this file was created.
