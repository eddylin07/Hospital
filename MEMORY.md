# MEMORY.md

## Project facts

- Project name: Hospital.
- Stack inferred on 2026-08-06: Java Spring Boot 2.0, MyBatis, Freemarker templates, Layui/static JavaScript, MySQL, Maven.
- Runtime configuration is in `src/main/resources/application.yml`; credential values must not be copied into memory.

## Audit focus

- Recurring high-severity paths: login session persistence, server-side role authorization, public registration role assignment, patient appointment ownership, prescription inventory updates, latest seek-row updates, and PDF/export null handling.
- Historical automation memory notes that Maven may be missing in Cursor Cloud and Java 21 may require dependency/test-runner compatibility fixes.

## Environment notes

- On 2026-08-06, `~/.codex/templates/` was absent in this cloud VM, so `AGENTS.md` and `MEMORY.md` were initialized manually from repository-visible facts.
