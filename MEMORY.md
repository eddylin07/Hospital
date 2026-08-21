# Project Memory

Project: Hospital
Last updated: 2026-08-21

## Inferred project facts

- Java Spring Boot hospital management system using Maven, MyBatis, and FreeMarker templates.
- Source code lives under `src/main/java/com/hospital`; SQL initialization is under `sql/hospital.sql`.
- Credential-containing configuration exists under `src/main/resources/application.yml`; remember the path only, never values.

## Investigation notes

- Prior critical investigations repeatedly found severe issues around login session persistence, role authorization, public registration, appointment ownership, prescription inventory updates, PDF null handling, and Java/Maven compatibility.
- When fixing code, validate with focused tests and `mvn test` when the toolchain is available.
- 2026-08-21 critical audit fixed failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR/generated-key binding, doctor-to-patient workflow authorization, prescription inventory overdraw/race/latest-seek-row updates, long prescription SQL truncation, empty input/PDF null crashes, PDF filename path traversal, orphan doctor workflow crashes, and Java/Maven test compatibility; `mvn test` passed with 22 tests.
