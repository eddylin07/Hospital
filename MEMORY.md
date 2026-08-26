# Project Memory

## Repository Facts

- Project name: Hospital.
- Stack inferred from repository: Java Maven, Spring Boot 2.0, MyBatis, FreeMarker, MySQL.
- The app is a medical information management system.

## Operational Notes

- Maven may be absent in the cloud image; install it before running `mvn test` if needed.
- Java 21 can expose compatibility issues with this old Spring Boot/Lombok stack.
- On 2026-08-26 in `cursor/critical-bug-investigation-f681`, high-risk fixes covered failed-login session persistence, role authorization, public search interceptor allowlisting, public admin self-registration, duplicate username registration binding, patient appointment IDOR/generated-key binding, doctor-patient workflow authorization, prescription inventory overdraw/race protection, latest-seek-only prescription updates, empty drug/option input crashes, and missing appointment/seek PDF null crashes. `mvn test` passed with 12 tests.

## Unknowns To Clarify

- Production Java version.
- Database version and migration process.
- Expected deployment/test environment.
