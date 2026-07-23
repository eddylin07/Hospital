# MEMORY.md

## Project facts

- Project name: Hospital.
- Stack: Java Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker templates, Layui/jQuery, MySQL.
- README describes the system as a medical information management system.

## Investigation notes

- This workspace did not contain `AGENTS.md` or `MEMORY.md` on entry, and `/home/ubuntu/.codex/templates` was absent, so minimal project-specific files were created directly.
- On `cursor/critical-bug-investigation-89bc`, fixed failed-login session persistence, server-side role authorization, public admin self-registration, prescription inventory overdraw, latest-seek-row corruption, and null seek printing; `mvn test` passed with 9 tests on 2026-07-23 after installing Maven in the cloud image.

## Open questions

- Local database credentials and seed data location are not documented.
