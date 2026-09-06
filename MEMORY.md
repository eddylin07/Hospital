# Project Memory

## Project Facts

- Project name: Hospital (`HospitalAction` Maven artifact).
- Stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven, MySQL.
- README identifies the project as a medical information management system.

## Working Memory

- 2026-09-06: `AGENTS.md` and `MEMORY.md` were initialized because both files were absent and `/home/ubuntu/.codex/templates/` was unavailable.
- Critical bug investigations in this repository should prioritize authentication/session handling, role authorization, appointment ownership, doctor-patient authorization, prescription inventory updates, PDF null handling, and Maven dependency compatibility.
- 2026-09-06: Fixed critical failed-login session persistence, server-side role authorization, public admin self-registration, duplicate username registration binding, patient appointment IDOR/generated-key binding, doctor workflow patient authorization, prescription inventory overdraw/race/partial-update protection, latest-seek-row prescription updates, empty drug/option input crashes, null patient/doctor detail handling, MyBatis multi-parameter binding, and Java 21 Maven test compatibility. `mvn test` passed with 13 focused tests.
