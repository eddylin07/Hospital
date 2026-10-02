# MEMORY.md

## Project notes

- Project name: Hospital.
- The repository is a Maven-based Spring Boot hospital management system using MyBatis, FreeMarker templates, MySQL, and Layui.
- Current high-risk areas for bug-finding runs: authentication/session handling, role authorization, registration, patient appointment ownership, doctor-patient workflow authorization, prescription inventory updates, PDF/export null handling, and Maven/Java compatibility.

## Session notes

- 2026-10-02: AGENTS.md and MEMORY.md were missing in the workspace; initialized both from inferred repository context because template files were unavailable under `~/.codex/templates/`.
- 2026-10-02: Fixed critical auth/session, role authorization, public admin self-registration, patient appointment ownership, doctor workflow patient authorization, prescription inventory overdraw/latest-seek updates, and PDF/input null-crash paths; `mvn test` passed with 12 tests.
