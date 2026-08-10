# Project Memory

## Project Overview

- Project: Hospital
- Repository root: `/workspace`
- Inferred stack: Java Maven application using Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL, Layui.
- README describes the app as a medical information management system.

## Operational Notes

- `AGENTS.md` and `MEMORY.md` were absent on 2026-08-09; template directory `/home/ubuntu/.codex/templates/` was also absent, so minimal project-local files were created from inferred repository facts.
- Do not store credential values in this file. If credentials are encountered, store only their file/path location.
- On branch `cursor/critical-bug-investigation-edba`, high-severity fixes were applied for failed-login session persistence, server-side role authorization, public admin self-registration, duplicate-username registration linking, patient appointment IDOR, prescription inventory overdraw/race protection, latest-seek-row prescription updates, and empty drug/option input crashes. `mvn test` passed with 12 tests on 2026-08-09.
- Follow-up fixes on `cursor/critical-bug-investigation-edba` added drug resultMap price/number hydration, appointment generated-key pointer updates, missing appointment/seek PDF guards, and doctor appointment-relationship checks for dispense/diagnosis/seek printing. `mvn test` passed with 17 tests on 2026-08-09.
- Credential fields are present in `src/main/resources/application.yml`; remember only the path, not the values.

## Open Questions

- Runtime deployment target, database seed process, and local integration-test data requirements are not documented in the repository.
