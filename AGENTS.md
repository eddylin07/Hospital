# Project Instructions

## Project overview

- Project: Hospital
- Stack: Java, Maven, Spring Boot 2.0.0, MyBatis
- Package root: `com.hospital`

## Development and testing

- Prefer focused unit tests for service, controller, utility, and mapper XML behavior.
- Existing environment note: Lombok 1.16.22 is not compatible with modern JDK module defaults. Use JDK 8 for Maven commands when available:
  - `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- Keep tests deterministic and independent; avoid requiring a live MySQL instance for unit-level coverage.

## Automation notes

- Add tests for meaningful business-risk paths before broad refactors.
- Production behavior should only change when a test exposes a clear defect.
