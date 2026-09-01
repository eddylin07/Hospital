# Project Memory

- Project: Hospital
- Initialized: 2026-09-01
- Stack inferred from repository: Java, Spring Boot 2.0, MyBatis, FreeMarker, Layui, Maven.
- The expected `~/.codex/templates/AGENTS.md` and `~/.codex/templates/MEMORY.md` templates were unavailable in this environment, so minimal project files were created.

## Notes

- Keep memory concise. Record durable architecture decisions, recurring pitfalls, user corrections, and external resource locations only.
- Do not store credential values; store locations only when needed.
- 2026-09-01 critical audit fixed failed-login session persistence, role-based interceptor authorization, public admin self-registration, patient appointment IDOR/generated-key binding, doctor workflow patient authorization, prescription inventory overdraw/race/latest-seek-row updates, empty input crashes, missing appointment/seek PDF crashes, drug price/stock result mapping, and MyBatis multi-parameter binding crashes. `mvn test` passed with 12 tests.
