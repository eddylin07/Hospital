# Project Memory

## Facts

- Project: Hospital.
- Stack: Java Spring Boot 2.0, MyBatis, FreeMarker, Maven, MySQL.
- Test command known to work in prior automation runs: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Lombok 1.16.22 requires Java 8 on the cloud runner; Java 21 compilation can fail on module access.
- Some fresh cloud images may require installing `maven` and `openjdk-8-jdk-headless` before tests can run.

## Coverage Automation Notes

- Prior coverage work often focused on mapper XML invariants, login/session behavior, interceptor access control, date formatting, and prescription/dispensing inventory logic.
- Prefer focused JUnit tests for high-risk business rules over broad snapshots.
- On branch `cursor/missing-test-coverage-9419`, added coverage for failed-login session handling, prescription mapper SQL, drug result mapping, all-or-nothing dispensing stock validation, and calendar-year date formatting; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 8 tests.

