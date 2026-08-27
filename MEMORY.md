# MEMORY.md

## Project facts

- Project name: Hospital.
- Stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven, MySQL.
- Repository purpose: medical information management system.

## Environment notes

- Created on 2026-08-27 because `AGENTS.md` and `MEMORY.md` were missing and `/home/ubuntu/.codex/templates/` was unavailable.
- Automation memory indicates Maven may be absent in Cursor Cloud; install Maven if needed before running `mvn test`.

## Investigation notes

- For critical bug investigations, prioritize auth/session handling, role authorization, patient appointment ownership, doctor-patient workflow authorization, prescription stock integrity, latest seek-row updates, PDF null handling, and build/test compatibility.
- On 2026-08-27, fixed prescription dispensing data integrity: `SeekMapper.updateDrugs` must update only the patient's latest seek row, `DrugsMapper.updateNumber` must atomically guard `number >= requested`, and `PatientServiceImpl.seek` must reject insufficient stock before writing patient/seek rows.
- On 2026-08-27, also fixed failed-login session persistence, role authorization, public admin self-registration, duplicate username registration binding, and patient appointment IDOR. `mvn test` passed with 14 tests.
