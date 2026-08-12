# Project Memory

- Project name: Hospital.
- Created: 2026-08-12.
- Confirmed stack: Maven Java app using Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker templates, MySQL connector, and Lombok 1.16.22.
- Testing note: use `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` when JDK 8 is installed because Lombok 1.16.22 can fail under modern JDK module rules.
- The repository may start without `src/test`; add focused tests under `src/test/java` following the production package names.
- On branch `cursor/missing-test-coverage-78b6`, regression tests cover login session handling, dispensing all-or-nothing inventory updates, latest-seek-only/null-safe prescription SQL, and calendar-year date formatting. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 7 tests.
