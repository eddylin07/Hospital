# MEMORY.md

## Project Memory

- Project name: Hospital.
- Repository stack inferred on 2026-10-06: Java Spring Boot 2.0, MyBatis, Freemarker, Maven, MySQL.
- `AGENTS.md` and `MEMORY.md` were missing; no templates existed under `~/.codex/templates/`, so both files were initialized from repository inspection.
- On 2026-10-06, critical auth boundary bugs were fixed: failed login request bodies are no longer persisted as sessions, role-based route authorization is enforced in `LoginInterceptor`, and public registration no longer creates admin accounts for blank certificate ids. `mvn test` passed with 9 tests.

## Open Questions

- None for the current automation task.
