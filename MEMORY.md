# Project Memory

## Project facts

- Project name: Hospital.
- Stack: Spring Boot 2.0.0.RELEASE, MyBatis, Maven, Freemarker/Layui, MySQL mapper XML.
- Main package: `com.hospital`.
- As of 2026-08-24, the repository did not include checked-in tests before coverage work.

## Environment notes

- Lombok 1.16.22 fails with modern Java module access restrictions. Run tests with JDK 8:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- Fresh environments may need Maven and JDK 8 installed before validation.

## Coverage focus

- High-risk areas for regression tests: login/session handling, authentication interceptor behavior, prescription/dispensing inventory updates, MyBatis mapper SQL, date formatting near year boundaries, and form parsing utilities.
- 2026-08-24 coverage run added focused tests for login session persistence, anonymous interceptor redirects, prescription inventory/price updates, latest-seek-only mapper SQL, and calendar-year date formatting. Validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Last updated

- 2026-08-24
