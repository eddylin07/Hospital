# MEMORY.md

## Project facts

- Hospital is a Java Maven application for medical information management.
- The stack is Spring Boot 2.0, MyBatis, FreeMarker, MySQL, PageHelper, Druid, FastJSON, Apache POI, and iText.
- Role ids used by the application are admin=1, doctor=2, patient=3.

## Known high-risk areas

- Authentication must only store a session after `LoginService.login` resolves a real user id and role.
- Server-side authorization must protect role-specific workflows; hiding frontend menus is not authorization.
- Public registration must not allow unauthenticated admin creation.
- Prescription dispensing must reject quantities greater than stock and use an atomic database guard to prevent negative inventory under concurrency.
- Dispensing updates must target the patient's latest seek row, not every historical seek row for that patient.
- Patient appointment creation must bind the patient id from the session rather than trusting request bodies.
- PDF/export endpoints should handle missing rows without crashing.
- Empty drug/option input can crash parsing paths if not validated.
- Maven may be absent in the cloud image; install it before running `mvn test` if needed.
- Older Lombok/Surefire/Spring Boot test dependencies can fail on Java 21; existing fixes in prior runs updated Lombok/Surefire and avoided Mockito for narrow tests.

## Current session

- Created this file on 2026-07-27 because the project root lacked `MEMORY.md` and the configured template directory was unavailable.
- On `cursor/critical-bug-investigation-58d2`, fixed failed-login session persistence, public admin self-registration, role authorization, prescription inventory overdraw/latest-seek-row updates, patient appointment IDOR, empty drug/option input crashes, missing appointment/seek PDF crashes, and Java 21 Maven test compatibility; `mvn test` passed with 8 tests on 2026-07-27 after installing Maven.
