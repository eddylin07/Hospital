# Project Memory

- Project: Hospital
- Created: 2026-07-17
- Stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL, Layui.

## Persistent Automation Notes

- Cursor automation memory contains prior notes about Java 21/Lombok build compatibility, Maven availability, Mockito/ByteBuddy limitations, role ids and authorization boundaries, login session safety, and prescription inventory concurrency.

## Session Notes

- 2026-07-17: `~/.codex/templates/` was not present in this environment, so AGENTS.md and MEMORY.md were created from inferred repository context.
- 2026-07-17: Critical fixes on `cursor/critical-bug-investigation-bf12` enforce real login id/role before storing sessions, add server-side role checks for admin/doctor/patient protected paths, block blank-cert public admin registration, and make prescription dispensing validate stock before atomic guarded deductions that update only the latest seek row.
- 2026-07-17: `mvn test` passes on Java 21 after updating Lombok to 1.18.46 and Surefire to 3.6.0-M1; the Cursor Cloud image needed Maven installed via apt.
