# MEMORY.md

## Project facts

- Project name: Hospital.
- Stack: Java Maven project using Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL, and SQL seed/schema under `sql/`.
- Main source tree: `src/main/java/com/hospital/`.
- Application entry point: `src/main/java/com/hospital/HospitalApp.java`.

## Working notes

- `AGENTS.md` and `MEMORY.md` were initialized on 2026-08-05 because no project-local copies existed and `/home/ubuntu/.codex/templates/` was unavailable.
- On `cursor/critical-bug-investigation-1272`, critical fixes covered failed-login session persistence, public admin self-registration, server-side role authorization, patient appointment IDOR, prescription inventory overdraw/race protection, and latest-seek-row prescription updates; `mvn test` passed with 9 tests on 2026-08-05.
- Keep memory concise; do not duplicate details that can be read directly from source.
