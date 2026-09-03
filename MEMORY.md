# Project Memory

## Facts

- Project name: Hospital.
- Technology stack: Java Spring Boot 2.0, MyBatis, FreeMarker, Layui, MySQL, Maven.
- The expected template directory `/home/ubuntu/.codex/templates` was absent during initialization on 2026-09-03, so AGENTS.md and MEMORY.md were created from repository inspection.

## Review Focus

- High-risk areas include login/session handling, role-based authorization, patient ownership checks, doctor-to-patient workflow authorization, prescription inventory updates, and PDF/export null handling.
- Prior automation memory notes indicate this repository has repeatedly needed Maven installation in Cursor Cloud before running `mvn test`.
- On 2026-09-03, branch `cursor/critical-bug-investigation-6c43` fixed critical session/role authorization, public admin self-registration, patient appointment IDOR/generated-key binding, doctor workflow patient authorization, prescription inventory overdraw/race/latest-seek-row updates, drug dispensing result mapping, and empty dispensing input crashes; `mvn test` passed with 22 tests on Java 8.

## Open Items

- No project-specific user-provided testing preferences have been recorded beyond the default Maven-based verification.
