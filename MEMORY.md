# MEMORY.md

## Project facts

- Project name: Hospital.
- Java web application using Spring Boot 2.0.0.RELEASE, MyBatis, Maven, Freemarker templates, and MySQL connector.
- Tests should use the existing Spring Boot test/JUnit 4 conventions.

## Environment notes

- Lombok 1.16.22 does not compile reliably on Java 21 because of javac module access restrictions.
- Use JDK 8 for validation: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Some fresh environments may need Maven and OpenJDK 8 installed before tests can run.

## Coverage automation notes

- Prior coverage runs often found meaningful risk around login session handling, prescription SQL, dispensing inventory/price updates, date formatting, interceptors, and mapper XML mappings.
- Keep tests deterministic and independent; avoid database-dependent tests unless using XML parsing or mocks.
- 2026-10-03 coverage run added regression tests for login session persistence, drug dispensing inventory/price calculation, latest-seek-only prescription SQL, and calendar-year date formatting; `mvn test` passed with 7 tests.

