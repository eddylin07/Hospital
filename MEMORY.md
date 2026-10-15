# MEMORY.md

## Project facts
- Project: Hospital
- Stack: Java, Spring Boot, MyBatis, Freemarker, Maven
- Main source roots: `src/main/java`, `src/main/resources`
- Test command: `mvn test`

## Operational notes
- 2026-10-15: `AGENTS.md` and `MEMORY.md` were missing from the repository root; no template files were available under the expected `~/.codex/templates/` path, so minimal project-specific files were initialized.
- Automation memory notes recurring critical areas in this repo: login/session persistence, server-side role authorization, public admin registration, patient appointment ownership, doctor workflow authorization, prescription inventory updates, latest seek-row targeting, null PDF generation paths, MyBatis parameter/result mappings, and Java 21 Maven test compatibility.
- 2026-10-15: Fixed critical auth/session, role authorization, public admin self-registration, doctor-patient workflow authorization, patient appointment IDOR/race, prescription stock overdraw/partial-write risk, latest seek-row drug updates, empty input and PDF null guards, and Java 21 Maven test compatibility. `mvn test` passed with 10 focused tests.
