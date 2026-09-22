# Project Memory

## Project facts

- Project name: Hospital.
- Date initialized: 2026-09-22.
- Repository: `https://github.com/eddylin07/hospital`.
- Stack inferred from `pom.xml`: Java, Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, Maven, MySQL.
- README describes this as a medical information management system.

## Environment notes

- `AGENTS.md` and `MEMORY.md` were missing at session start.
- `~/.codex/templates/` was not present in this environment, so minimal project-local files were created from inferred facts.

## Security and data-integrity notes

- Login sessions are valid only after `LoginService.login` returns a success message and server-hydrates both `id` and `role`; request-body `id` / `role` must not be trusted.
- Role ids are admin=1, doctor=2, patient=3. `/admin/**`, `/patient/**`, and doctor workflow routes require server-side role enforcement, while public search routes remain allowlisted.
- Public registration must not create admin accounts from blank certificate ids.
- Patient appointment creation must bind `patientid` to the patient resolved from the current session and use the generated appointment id for the patient pointer.
- Prescription dispensing must use guarded stock updates and only update the latest seek row for the patient.
- Validation on 2026-09-22: `mvn test` passed with 12 tests.
