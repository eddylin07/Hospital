# Hospital project memory

Initialized: 2026-09-23

## Project facts

- Repository name: Hospital.
- Application type: Java/Maven Spring Boot hospital information management system.
- Persistence stack: MyBatis with MySQL schema in `sql/hospital.sql`.
- View layer: FreeMarker templates.

## Investigation notes

- `~/.codex/templates/AGENTS.md` and `~/.codex/templates/MEMORY.md` were not present in this environment, so minimal project-local files were initialized from repository inspection.
- For critical bug sweeps, prioritize concrete triggers that can cause auth bypass, data loss/corruption, crashes in critical paths, or broad user-facing breakage.
- 2026-09-23: Found recurring critical auth/session, role authorization, public registration, patient appointment IDOR, doctor workflow IDOR, prescription inventory corruption/race, and PDF/null-input crash issues on `origin/master`; fixed them with focused tests and `mvn test` passed with 12 tests.

## Credentials

- No credential values are stored here. Record only credential locations if needed.
