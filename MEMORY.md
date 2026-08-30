# MEMORY.md

## Project facts

- Project name: HospitalAction
- Date initialized: 2026-08-30
- Stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven, MySQL
- Current automation branch at initialization: `cursor/critical-bug-investigation-f387`

## Working notes

- `~/.codex/templates/` was not present in this cloud image when `AGENTS.md` and `MEMORY.md` were initialized, so minimal project-specific files were created directly.
- Critical bug investigations in this repository should prioritize authentication/session handling, role authorization, registration privilege boundaries, appointment ownership, doctor-patient workflow authorization, prescription inventory updates, PDF null handling, and Java/Maven test compatibility.
- On `cursor/critical-bug-investigation-f387`, fixed failed-login session persistence, server-side role authorization, public admin self-registration, duplicate username registration binding, patient appointment IDOR/generated-key binding, doctor workflow patient authorization, prescription inventory overdraw/race/partial-update protection, latest-seek-row prescription updates, empty drug/option input crashes, missing appointment/seek PDF crashes, MyBatis multi-parameter binding crashes, and Maven dependency/test compatibility; `mvn test` passed with 14 tests on 2026-08-30.
- Do not store credential values here; record only credential locations if needed.
