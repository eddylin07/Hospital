# Project Memory

## Project facts

- Project name: Hospital.
- Application type: medical information management system.
- Stack: Java, Spring Boot 2.0.0, MyBatis, Freemarker, MySQL, Maven, Lombok.
- Role ids used by the application: admin=1, doctor=2, patient=3.

## Critical areas to review

- Authentication and session creation: failed logins must not create authenticated sessions.
- Authorization: protected `/admin/**`, `/doctor/**` workflow endpoints, and `/patient/**` paths require matching server-side role checks.
- Registration: public registration must not create admin accounts from blank or missing certificate ids.
- Prescription dispensing: inventory updates must reject requests above current stock and use an atomic SQL guard to prevent negative inventory under concurrency.
- Dispensing side effects should target the patient's latest seek row, not every historical seek row.

## Environment notes

- The legacy Lombok/Spring Boot test stack may fail on Java 21 unless dependencies are updated.
- Maven may need to be installed in the cloud image before running tests.
