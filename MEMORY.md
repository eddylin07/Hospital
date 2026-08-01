# Project Memory

## Project Profile

- Project: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven, MySQL
- Date initialized: 2026-08-01

## Known Audit Context

- Prior automation memory records recurring critical findings in this repository:
  - Failed login attempts must not be persisted into session state.
  - Server-side role authorization is required for admin, doctor, and patient routes.
  - Public registration must not create admin accounts.
  - Prescription dispensing must reject overdraws and avoid updating historical seek rows.
  - Patient appointment creation must bind the patient id from the authenticated session.
- Maven may not be preinstalled in the cloud image.
- Java 21 can expose compatibility issues with the old Lombok/Spring Boot test stack.

## Update Log

- 2026-08-01: Initialized local memory because `~/.codex/templates/` was unavailable in this environment.
- 2026-08-01: Fixed critical failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR, prescription inventory overdraw/race, and latest-seek-row update bugs on `cursor/critical-bug-investigation-98ce`; `mvn test` passed with 11 tests after installing Maven.
