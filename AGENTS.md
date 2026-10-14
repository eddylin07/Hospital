# AGENTS.md

## Project

- Name: Hospital
- Stack: Java, Maven, Spring Boot 2.0.0, MyBatis, Freemarker
- Date initialized: 2026-10-14

## Working rules

- Read `MEMORY.md` before making code changes.
- Prefer existing Maven and Spring test conventions.
- Keep regression tests deterministic and scoped to behavior with business risk.
- Do not store credentials or secret values in this repository; record only their locations when needed.

## Verification

- Use the narrowest relevant Maven test target for changed areas.
- If a test requires unavailable infrastructure, document the limitation and avoid adding fragile tests.
