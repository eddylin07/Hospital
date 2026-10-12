# Project Memory

## Project facts

- Repository name: Hospital.
- Application purpose from README: hospital/medical information management system.
- Inferred stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, MySQL.

## Notes

- `AGENTS.md` and `MEMORY.md` were missing on 2026-10-12. Template files were not present at `/home/ubuntu/.codex/templates`, so minimal local versions were initialized from repository metadata.
- On 2026-10-12, critical fixes were applied for failed-login session persistence, server-side role authorization, public admin self-registration, patient appointment IDOR, doctor workflow patient authorization, prescription inventory overdraw/race and latest-seek-row updates, MyBatis multi-parameter binding crashes, and missing appointment/seek PDF null crashes. `mvn test` passed with 8 focused tests.
