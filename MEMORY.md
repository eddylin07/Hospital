# Project Memory

## Repository Facts

- Project name: Hospital.
- Application: medical/hospital information management system.
- Stack inferred from `pom.xml` and `README.md`: Spring Boot 2.0, MyBatis, FreeMarker, Layui, Maven, MySQL.
- MyBatis generator configuration is in `gener.xml`; it contains local database connection settings, so remember the file location only, not credential values.
- Template directory `~/.codex/templates/` was absent in this cloud environment on 2026-07-22, so AGENTS.md and MEMORY.md were bootstrapped from inferred project facts.

## High-Risk Areas

- Server-side authorization is critical: role ids are admin=1, doctor=2, patient=3.
- Login sessions should only be stored after a successful login has a real user id and role.
- Public registration must not create admin users based on blank or missing certificate ids.
- Prescription dispensing must reject quantities above stock and should use an atomic database update guard to prevent negative inventory under concurrency.
- Dispensed drug updates should target the latest seek row for a patient, not every historical seek row.
- On `cursor/critical-bug-investigation-55ce`, fixed failed-login session persistence, server-side role authorization, public admin self-registration, prescription inventory overdraw, latest-seek-row updates, and null seek printing; `mvn test` passed with 7 tests on 2026-07-22.

## Environment Notes

- Maven may be missing in Cursor Cloud images; install it before running `mvn test` if needed.
- Legacy Lombok/Spring Boot test dependencies may fail on modern JDKs unless dependency versions are updated.
