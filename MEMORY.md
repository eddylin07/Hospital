# MEMORY.md

## Project facts

- Project name: Hospital.
- Repository path: `/workspace`.
- Java Maven application using Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, and MySQL.
- README describes the app as a hospital information management system.

## Testing notes

- Run `mvn test` for automated validation.
- Maven may be absent in the cloud image; install it before testing if needed.

## Security notes

- Credential-like configuration lives under `src/main/resources/`; remember paths only, never secret values.

## Session notes

- 2026-08-14: `AGENTS.md` and `MEMORY.md` were missing; `/home/ubuntu/.codex/templates/` was unavailable, so minimal inferred files were created from repository facts.
- 2026-08-14: On branch `cursor/critical-bug-investigation-ebfa`, fixed critical failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR, doctor-to-patient workflow authorization, prescription inventory overdraw/race/latest-seek-row corruption, empty drug/option input crashes, missing appointment/seek PDF crashes, drug price/stock hydration, and generated appointment id handling. `mvn test` passed with 14 tests on Java 8.
