# MEMORY.md

## Project facts

- Project name: Hospital.
- Application type: medical information management system.
- Main stack inferred from `pom.xml` and `README.md`: Java, Maven, Spring Boot 2.0, MyBatis, Freemarker/Layui, MySQL.

## Review focus

- Critical audit areas for this codebase: login/session integrity, server-side role authorization, patient appointment ownership, doctor-to-patient workflow authorization, prescription inventory consistency, PDF/null handling, and database field truncation.
- Credential values must not be copied into memory. If needed, reference only the path `src/main/resources/application.yml`.

## Environment notes

- Validate Java changes with `mvn test` when Maven is available.

## 2026-08-22 critical audit notes

- Fixed critical auth/session, role authorization, patient appointment ownership, doctor-patient workflow authorization, and prescription inventory/seek-row corruption issues on branch `cursor/critical-bug-investigation-f4b2`.
- `mvn test` passed with 12 tests after adding focused regression coverage.
