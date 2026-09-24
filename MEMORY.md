# Project Memory

## Project Snapshot

- Project: Hospital
- Stack: Java, Maven, Spring Boot 2.0, MyBatis, Freemarker, MySQL.
- Purpose: Hospital / medical information management system.

## Operational Notes

- `src/main/resources/application.yml` contains configuration fields that may include credentials; remember only the path, never values.
- Maven may be missing in the cloud image. Install it before running `mvn test` if needed.
- Java 21 can expose compatibility issues with older Lombok / Mockito / Surefire versions in this codebase.

## Security / Correctness Focus Areas

- Authentication sessions must only be stored after server-side login success hydrates a real id and role.
- Server-side authorization matters for `/admin/**`, `/patient/**`, and doctor workflow endpoints; menu hiding is not authorization.
- Patient appointment creation must bind to the patient resolved from the session, not request-provided patient ids.
- Prescription dispensing must reject stock overdraw and update stock atomically.
- Prescription updates to `seek` must target the patient's latest seek row, not every historical row for the patient.
- PDF generation paths should handle missing records without null dereferences.
