# AGENTS.md

## Project

- Name: workspace
- Primary stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, MySQL
- Generated on: 2026-08-17

## Working guidelines

- Read `MEMORY.md` before making project changes.
- Keep code changes minimal and scoped to the requested behavior.
- Prefer existing Spring Boot/MyBatis patterns already present in the repository.
- Do not commit secrets or credential values; record only their locations when necessary.

## Testing guidelines

- Use Maven for build and test validation when dependencies are available.
- Add or update focused tests for behavioral fixes when practical.
- If full Maven validation is blocked by the environment, document the exact command and failure.
