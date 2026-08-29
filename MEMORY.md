# Hospital Memory

## Project facts

- Project name: Hospital.
- Initialized locally on 2026-08-29 because `AGENTS.md` and `MEMORY.md` were missing and no templates existed under `~/.codex/templates/`.
- Stack inferred from repository files: Spring Boot 2.0, Maven, MyBatis, Freemarker, Layui/jQuery, MySQL.
- Datasource credentials are configured in `src/main/resources/application.yml`; do not copy credential values into memory.

## Automation context

- Daily critical-bug investigation should inspect recent behavioral changes and only open a PR for concrete high-severity bugs with a minimal validated fix.
- If no critical bug is found, avoid opening a PR for speculative or low-severity findings.
- On `cursor/critical-bug-investigation-4164`, recent login/interceptor and seek/dispensing changes still contained critical auth bypass including caller-supplied forged `id/role` on failed login, role bypass, public admin self-registration, patient appointment IDOR, doctor workflow patient authorization gaps, prescription inventory overdraw/race/partial-deduct risk, and seek history over-update; `mvn test` passed with 17 tests on 2026-08-29.
