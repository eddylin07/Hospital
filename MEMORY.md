# MEMORY.md

## Project facts

- Project name: Hospital.
- Initialized for agent memory on 2026-07-21.
- Java/Spring Boot 2.0.0.RELEASE Maven application using MyBatis, FreeMarker, MySQL, and LayUI resources.

## Working notes

- High-severity bug investigations should focus on authentication/authorization, patient/doctor/admin role boundaries, prescription inventory writes, and data mutation endpoints.
- Existing automation memory notes indicate Maven may be absent in Cursor Cloud images; install Maven before running tests if `mvn` is unavailable.
- Existing automation memory notes indicate old Lombok and Spring Boot test dependencies can fail on Java 21 unless branch fixes or non-Mockito tests are used.
- On `cursor/critical-bug-investigation-d5fb`, fixed failed-login session persistence, role authorization, public admin self-registration, and prescription inventory/latest-seek update corruption; `mvn test` passed with 9 tests on 2026-07-21.

## Credentials

- No credential values recorded.
