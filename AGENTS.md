# AGENTS.md

## Project

- Name: Hospital
- Type: Java web application
- Stack: Spring Boot 2.0, MyBatis, Freemarker, Layui, Maven
- Date initialized: 2026-08-18

## Working rules

- Default to minimal, high-confidence changes with focused tests.
- Do not record credential values; if needed, record only their file paths.
- For security/correctness work, trace the full request path from controller to service and mapper before changing code.
- Prefer existing project patterns over broad refactors.

## Validation

- Use Maven for automated tests when available: `mvn test`.
- If Maven is missing in the cloud image, install it with apt before running tests.

## Unknown project details

- Local database/service startup procedure is not documented in this repository.
- No template was available at `~/.codex/templates/` when this file was initialized.
