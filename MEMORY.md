# MEMORY.md

## Project Facts

- Project name inferred from workspace path: `workspace`.
- Stack inferred on 2026-10-03: Java Maven application using Spring Boot 2.0, MyBatis, Freemarker templates, and MySQL connector.
- Source layout: `src/main/java/com/hospital`, resources under `src/main/resources`.

## Operating Notes

- Template files were not present under `~/.codex/templates/` during initialization, so this file and `AGENTS.md` were created as minimal project-local defaults.
- Credentials should be recorded by location only, never by value.
- On 2026-10-03, critical auth fixes were applied: failed logins must not populate `session.login`, protected paths enforce role ids admin=1/doctor=2/patient=3 server-side, and public `/regest` cannot create admin users or bind duplicate usernames.

## Unknowns To Confirm

- Canonical project name and owner.
- Preferred validation command beyond Maven defaults.
