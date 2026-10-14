# MEMORY.md

## Project facts

- Project name: Hospital.
- Tech stack inferred on 2026-10-14: Java, Spring Boot 2.0, Maven, MyBatis, Freemarker, MySQL.

## Working notes

- `AGENTS.md` and `MEMORY.md` were missing on 2026-10-14; `/home/ubuntu/.codex/templates` was also absent, so minimal files were generated from repository-visible information.
- For critical bug investigations, prioritize concrete trigger scenarios that can cause data loss, crashes, security holes, or significant user-facing breakage.
- If Maven is unavailable in the cloud image, install it before running Java tests.
- On `cursor/critical-bug-investigation-fcd6`, fixed failed-login forged-session persistence, role authorization with public search/static/error allowlisting, public admin self-registration/duplicate username binding, patient appointment IDOR/generated-key binding, doctor workflow patient authorization, prescription inventory overdraw/race/latest-seek-row updates, empty drug input crashes, missing appointment/seek PDF crashes, MyBatis appointment multi-parameter binding, and Java/Maven test compatibility; `mvn test` passed with 11 tests on 2026-10-14.
