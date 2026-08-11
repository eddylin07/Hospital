# MEMORY.md

## Project facts

- Project name inferred from workspace path: workspace.
- Repository appears to be Hospital.
- Stack inferred from repository layout: Java Maven application with MyBatis XML mappers, Freemarker templates, static JavaScript/CSS assets, and SQL bootstrap files.
- AGENTS.md and MEMORY.md templates were not present under `/home/ubuntu/.codex/templates/` when initialized on 2026-08-11, so minimal project files were created directly.

## Operating notes

- Read AGENTS.md and this file before modifying project files.
- Keep credentials out of memory; record only locations if needed.
- On branch `cursor/critical-bug-investigation-e6d7`, critical fixes covered failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR, prescription inventory overdraw/race/partial-update protection, latest-seek-row prescription updates, empty drug/option input crashes, null seek printing, and Java/Maven test compatibility; `mvn test` passed with 10 tests on 2026-08-11.

## Unknowns

- Exact local start command and required services are not yet confirmed.
