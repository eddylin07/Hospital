# MEMORY.md

## Project facts
- Project name: Hospital.
- Repository appears to be a Java Maven web application for a medical information management system.
- Main stack inferred from `pom.xml`: Spring Boot 2.0, MyBatis, FreeMarker, Layui, MySQL.
- Main application entry point: `src/main/java/com/hospital/HospitalApp.java`.

## Workflow notes
- `AGENTS.md` and `MEMORY.md` were absent on 2026-10-11.
- The expected template directory `~/.codex/templates/` was unavailable in this environment, so both files were initialized from repository inspection.

## Testing notes
- Use Maven commands for Java validation when dependencies/environment allow it.
- On 2026-10-11, `mvn test` passed on Java 8/Maven 3.8.7 with 10 focused tests after fixing auth/session, role authorization, patient appointment binding, doctor-patient workflow authorization, and prescription inventory/latest-seek-row updates.

## Security notes
- Failed login attempts must not remain in session, even if the request body contains forged `id` or `role`.
- Public registration must only bind existing doctors or patients by certificate id; public admin self-registration is not allowed.
- Patient appointment creation must derive `patientid` from the session login, not the request body.
- Doctor workflow routes that mutate or print patient data must verify the patient is assigned to the current doctor.
- Prescription dispensing must atomically guard inventory with `number >= requested` and update only the patient's latest seek row.
