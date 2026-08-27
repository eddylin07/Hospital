# AGENTS.md

Project: Hospital
Date initialized: 2026-08-27

## Project overview

- Medical information management system.
- Java Maven application using Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, and Layui.

## Working rules

- Follow existing Maven/Spring Boot project structure and naming conventions.
- Prefer focused, deterministic tests that cover meaningful business behavior.
- Keep production changes minimal; avoid behavior changes unless required for testability.
- Use credentials only by location/reference; do not record secret values.

## Validation

- For Java changes, run the most focused Maven test target available.
- If a full test suite is practical, run `mvn test`.

## Open items

- The expected template files under `~/.codex/templates/` were not present in this environment, so this file was initialized from repository inspection.
