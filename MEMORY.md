# MEMORY.md

## Project facts

- Project name: Hospital.
- Repository path: `/workspace`.
- Java Maven application using Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, and MySQL.
- README describes the app as a hospital information management system.

## Testing notes

- Run `mvn test` for automated validation.
- Maven may be absent in the cloud image; install it before testing if needed.

## Security notes

- Credential-like configuration lives under `src/main/resources/`; remember paths only, never secret values.

## Session notes

- 2026-08-14: `AGENTS.md` and `MEMORY.md` were missing; `/home/ubuntu/.codex/templates/` was unavailable, so minimal inferred files were created from repository facts.
