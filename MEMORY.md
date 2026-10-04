# MEMORY.md

## Project facts

- Initialized on 2026-10-04 because the workspace did not contain `AGENTS.md` or `MEMORY.md`, and template files were unavailable under `~/.codex/templates/`.
- Project name defaults to `Hospital` from the repository name and README.
- The app is a Java/Maven hospital information management system using Spring Boot 2.0, MyBatis, FreeMarker, and MySQL.

## Operational notes

- Read this file at session start before changing project files.
- Add durable lessons here when they are not already obvious from source code.
- Never record secret values; record only where secrets are configured.

## 2026-10-04 bug-fix notes

- Fixed high-severity auth/session issues: failed logins clear `id`/`role` and do not persist `login` in session; protected admin, doctor workflow, and patient paths now enforce role checks in `LoginInterceptor`.
- Public `/regest` no longer creates admin accounts for blank `certId`; admin accounts should be created through authenticated admin endpoints.
- Prescription dispensing now validates all requested drug quantities before writing, rejects over-stock requests, uses an atomic inventory guard, and updates only the patient's latest `seek` row.
- Validation: `mvn test` passed with 7 focused tests on 2026-10-04.

