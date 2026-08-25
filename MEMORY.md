# Project Memory

- Project name: Hospital.
- Initialized on 2026-08-25 because `AGENTS.md` and `MEMORY.md` were missing and `/home/ubuntu/.codex/templates` was unavailable.
- Stack inferred from `pom.xml`: Java, Spring Boot 2.0, MyBatis, Maven, Freemarker, MySQL.
- Critical-bug investigation should prioritize auth/session authorization, patient/doctor IDOR paths, prescription inventory integrity, PDF/null crash paths, and Maven/Java compatibility based on recurring automation history.
- 2026-08-25: Fixed critical auth and data-integrity issues. Login sessions must require a populated server-side id/role, public registration must not create admins from blank cert ids, patient appointment creation must bind to the session patient and use the generated appointment id, doctor workflow mutations/reads must verify the patient is assigned to the current doctor, and prescription dispensing must atomically guard stock and update only the latest seek row.
