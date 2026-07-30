# Project Memory

## Project Facts

- Project name: Hospital.
- Repository root: `/workspace`.
- Java Maven application using Spring Boot 2.0, MyBatis, Freemarker, and MySQL.
- Main Maven descriptor: `pom.xml`.
- Application entry point inferred from source: `src/main/java/com/hospital/HospitalApp.java`.
- MyBatis generator configuration exists at `gener.xml`; it contains local database connection settings, so do not copy credential values elsewhere.

## Review Notes

- For deep bug-finding automation, focus on recent behavioral changes and only open a PR for concrete critical bugs with a high-confidence minimal fix.
- On branch `cursor/critical-bug-investigation-8c11`, fixed critical failed-login session persistence, server-side role enforcement, public admin self-registration, patient appointment IDOR, prescription inventory/seek-row corruption, empty drug/option crashes, Linux PDF output path handling, and Java 21 Maven test compatibility; `mvn test` passed with 7 tests on 2026-07-30.
