# Project Memory

## Project facts

- Project name: Hospital.
- Tech stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven.
- Source layout: application code under `src/main/java`; no tests were present when this memory file was created.

## Security and correctness audit focus

- Historical high-risk areas include login session handling, server-side role authorization, public registration role assignment, patient appointment ownership, doctor-to-patient workflow authorization, prescription inventory updates, latest seek-row updates, and PDF generation on missing records.
- Credential configuration exists under `src/main/resources/application.yml`; remember the path only, not values.

## Environment notes

- Run focused verification with `mvn test` when Maven is available.
- Some cloud images may require installing Maven before tests can run.

## 2026-08-15

- `AGENTS.md` and `MEMORY.md` were absent. The expected template files under `~/.codex/templates/` were unavailable, so minimal inferred files were created from repository metadata.
- On `cursor/critical-bug-investigation-a6a7`, fixed failed-login session persistence, server-side role authorization for admin/patient/doctor workflow paths, public admin self-registration, patient appointment IDOR, generated appointment id usage, doctor-to-patient workflow authorization, prescription inventory overdraw/race protection, latest-seek-row prescription updates, empty option/drug selection crashes, missing appointment/seek PDF crashes, Linux-friendly PDF output defaults, and Java/Maven test compatibility. `mvn test` passed with 11 tests on 2026-08-15.
