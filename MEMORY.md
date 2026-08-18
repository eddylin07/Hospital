# MEMORY.md

## Project memory

- Project name: Hospital.
- Initialized on 2026-08-18 because both AGENTS.md and MEMORY.md were absent and `~/.codex/templates/` was unavailable.
- Tech stack inferred from repository files: Java, Maven, Spring Boot 2.0, MyBatis, Freemarker, Layui.
- High-risk review areas for this system: authentication/session handling, server-side role authorization, patient appointment ownership, doctor-to-patient access checks, prescription inventory consistency, PDF generation null handling, and schema-driven data truncation.

## Operational notes

- Do not store credential values in memory. If credential context is needed, store only the file path.
- Prefer `mvn test` for validation after code changes; install Maven in the cloud image if it is missing.

## Audit history

- 2026-08-18 on `cursor/critical-bug-investigation-79b4`: fixed critical failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR/generated-key binding, doctor workflow authorization, prescription inventory overdraw/race/latest-seek-row updates, empty input/PDF null crashes, SQL prescription truncation, Linux PDF paths, and Maven compatibility. `mvn test` passed with 20 tests on Java 8 / Maven 3.8.7.
