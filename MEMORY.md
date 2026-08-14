# MEMORY.md

## Project facts

- Project name: Hospital.
- Repository appears to be a Maven-based Java Spring Boot 2.0 medical information management system.
- Main source tree: `src/main/java/com/hospital`.
- Templates and static assets are under `src/main/resources`.

## Testing notes

- Use Maven for Java test execution.
- Full application/integration testing may require database configuration; exact local service setup is not documented yet.
- 2026-08-14 coverage run: JDK 8 with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed 8 JUnit4 tests covering login session handling, dispensing inventory/price validation, latest-seek prescription SQL, and calendar-year date formatting.

## Pending clarifications

- Database/service setup for end-to-end tests.
- Any project-specific code style or coverage expectations beyond existing repository conventions.
