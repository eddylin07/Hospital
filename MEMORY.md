# MEMORY.md

## Project facts
- Project: Hospital.
- Last updated: 2026-10-10.
- Stack: Java/Spring Boot 2.0.0 application using MyBatis, Freemarker templates, Maven, and MySQL.
- Repository contains SQL seed/schema files under `sql/` and Java source under `src/main/java/com/hospital`.

## Workflow notes
- Validate Java changes with Maven where possible.
- Login/session and interceptor changes are high-risk because the app has role-specific admin, doctor, and patient pages.
- 2026-10-10 critical audit fixed recurrent high-risk paths: failed login forged sessions, missing server-side role checks, public blank-cert admin self-registration, patient appointment IDOR, doctor-to-patient workflow authorization, all-history seek prescription updates, inventory overdraw/race protection, missing appointment/seek PDF null handling, and Maven test/build dependency pinning. `mvn test` passed with 14 focused tests.
