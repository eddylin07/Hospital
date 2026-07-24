# AGENTS.md

## Project

- Name: Hospital
- Repository root: `/workspace`
- Default development branch for this task: `cursor/missing-test-coverage-9350`
- Base branch: `master`

## Tech stack

- Java Spring Boot 2.0.0.RELEASE
- Maven
- MyBatis
- Freemarker
- JUnit 4 / Spring Boot test conventions

## Working rules

- Read `AGENTS.md` and `MEMORY.md` before making project changes.
- Keep changes small and aligned with existing code and test conventions.
- Prefer deterministic unit or mapper-level tests for regression coverage.
- Do not record secret values in project memory; record only locations or operational notes.
- Commit and push completed work to the configured task branch.

## Validation notes

- This repository may require Java 8 for Maven validation because of the checked-in Lombok version.
- Preferred validation command when Java 8 is installed:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`

## Unknown / to confirm

- No template files were available at `/home/ubuntu/.codex/templates` in this environment.
