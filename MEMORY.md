# Project Memory

## Project Facts

- Project name: workspace (repository: Hospital).
- Stack: Java, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, Maven.
- Purpose: medical information management system.
- Initialized from inferred repository state on 2026-08-16 because `~/.codex/templates/AGENTS.md` and `~/.codex/templates/MEMORY.md` were unavailable.

## Testing

- Use Maven tests for backend changes when possible.
- Existing automation memory notes indicate Maven may be missing in the cloud image; install Maven before `mvn test` if needed.

## Security Notes

- Credential-bearing configuration may exist under `src/main/resources/application.yml`; record only paths, never secret values.
