# MEMORY.md

## Project facts

- Project: Hospital.
- Inferred stack: Java, Spring Boot 2.0, Maven, MyBatis, FreeMarker templates, MySQL, Layui.
- Main application entry point: `src/main/java/com/hospital/HospitalApp.java`.
- Database initialization script: `sql/hospital.sql`.

## Audit focus

- Historical high-risk areas include login/session handling, server-side role authorization, public registration, patient appointment ownership, doctor-to-patient authorization, prescription inventory consistency, MyBatis parameter/result mappings, and PDF generation null handling.
- `SeekMapper.updateDrugs` must update only the latest seek row for a patient; a patientid-only `WHERE` rewrites all historical visits for that patient.
- Credential-bearing configuration exists under `src/main/resources/`; remember paths only, not values.

## Testing

- Prefer `mvn test` for verification when code changes are made.
- Maven may need installation in the cloud image before tests can run.
