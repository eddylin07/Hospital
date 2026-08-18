# MEMORY.md

## Project memory

- Project name: Hospital.
- Initialized on 2026-08-18 because both AGENTS.md and MEMORY.md were absent and `~/.codex/templates/` was unavailable.
- Tech stack inferred from repository files: Java, Maven, Spring Boot 2.0, MyBatis, Freemarker, Layui.
- High-risk review areas for this system: authentication/session handling, server-side role authorization, patient appointment ownership, doctor-to-patient access checks, prescription inventory consistency, PDF generation null handling, and schema-driven data truncation.

## Operational notes

- Do not store credential values in memory. If credential context is needed, store only the file path.
- Prefer `mvn test` for validation after code changes; install Maven in the cloud image if it is missing.
