# HospitalAction Memory

## Project facts

- Project name defaults to the workspace Maven artifact: `HospitalAction`.
- Stack inferred on 2026-09-04: Java, Maven, Spring Boot 2.0, MyBatis, Freemarker, MySQL.
- The expected template directory `~/.codex/templates/` was not present in this Cursor Cloud environment, so `AGENTS.md` and `MEMORY.md` were initialized with minimal local content.

## Review notes

- No credentials or secret values should be written here; record locations or procedures only.
- 2026-09-04 critical review fixed high-impact auth and data-integrity paths: failed login must not persist forged id/role, role checks belong in the interceptor, patient appointments must bind to the session patient, doctor workflow mutations require an appointment with the target patient, and prescription dispensing needs transactional atomic stock guards plus latest-seek-only updates.
