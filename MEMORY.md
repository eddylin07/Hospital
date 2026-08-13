# Project Memory

## Project facts

- Project name: Hospital.
- Stack observed on 2026-08-13: Maven-based Java Spring Boot 2.0 application using MyBatis, FreeMarker templates, Layui/static assets, and MySQL connector.
- Root files initially lacked `AGENTS.md` and `MEMORY.md`; `/home/ubuntu/.codex/templates` was unavailable, so minimal project-local files were created from inferred repository facts.

## Operational notes

- Follow `AGENTS.md` for future work in this repository.
- Credentials: record only configuration locations, never secret values.
- On 2026-08-13, current `master` still had critical auth/data-integrity bugs: failed-login sessions, missing server-side role checks, public blank-cert admin registration, patient appointment IDOR, doctor cross-patient workflow access, prescription overdraw/race risk, missing drug price/stock result mapping, appointment `MAX(id)` race, and latest-seek prescription corruption.
- Java 8 and Maven 3.8.7 are available in this cloud image; `mvn test` is the primary validation command after fixes.
