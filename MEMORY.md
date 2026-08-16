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
- On `cursor/critical-bug-investigation-7307`, fixed critical auth/session, role authorization, public admin registration, patient appointment IDOR, doctor workflow ownership, prescription inventory overdraw/race/partial-update/latest-seek corruption, malformed input crashes, missing PDF source crashes, long prescription truncation, and Java/Maven test compatibility; `mvn test` passed with 20 tests on 2026-08-16.
