# Project Memory

## Project Facts

- Project name: Hospital.
- Stack inferred from `pom.xml`: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven, MySQL.
- README describes the app as a medical information management system.
- Credential-bearing configuration exists under `src/main/resources/application.yml`; remember only the path, never credential values.

## Automation Notes

- Created from local inference on 2026-08-10 because `~/.codex/templates/` was not present in the cloud environment.
- Historical automation memory shows recurring critical areas: failed-login session persistence, role authorization, public admin registration, patient appointment IDOR, prescription inventory overdraw/races, latest-seek-row corruption, doctor/patient access checks, PDF null crashes, empty selection parsing, and Java/Maven test compatibility.
