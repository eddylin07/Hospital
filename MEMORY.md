# MEMORY.md

## Project facts

- Project name: Hospital.
- Inferred stack: Java Spring Boot 2.0, Maven, MyBatis, Freemarker, MySQL.
- Role ids used by the application: admin=1, doctor=2, patient=3.

## Operational notes

- Run `mvn test` for Java changes when possible.
- Fresh Cursor Cloud environments may not have Maven installed.
- Do not record credential values; only record credential locations when needed.

## Investigation notes

- 2026-10-08: Initialized AGENTS.md and MEMORY.md because both were absent and templates were unavailable under `~/.codex/templates/`.
- 2026-10-08: Fixed critical auth/data-integrity regressions: failed-login sessions, role authorization, public admin self-registration, patient appointment IDOR/generated-key binding, doctor workflow patient authorization, prescription overdraw/atomic stock update/latest-seek update, PDF null/path failures, and Java/Maven test compatibility. `mvn test` passed with 10 tests.
