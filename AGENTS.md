# Agent Instructions

Project: Hospital
Date initialized: 2026-08-10

## Project overview

- Java Maven project for a hospital/medical information management system.
- Stack inferred from `pom.xml`: Spring Boot 2.0.0, Spring MVC, MyBatis, FreeMarker, MySQL connector, PageHelper, FastJSON, POI, iText.
- Main application class: `com.hospital.HospitalApp`.

## Development guidelines

- Keep changes scoped to the requested behavior.
- Follow existing package structure under `src/main/java/com/hospital`.
- Prefer deterministic automated tests for regression coverage.
- Do not change production behavior unless required for testability or a clearly identified bug fix.

## Testing

- Primary test command: `mvn test`.
- Add tests under `src/test/java` following the production package structure.
- Prefer focused unit tests for utilities and edge-case logic; use Spring context tests only when framework integration is the behavior under test.

## Unknowns to confirm when needed

- No project-specific CI command has been identified beyond Maven tests.
- No database-backed local test fixture convention has been identified yet.
