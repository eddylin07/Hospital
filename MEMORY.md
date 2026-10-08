# MEMORY.md

## Project Notes

- Hospital is a Maven-based Spring Boot 2.0.0/MyBatis/Freemarker Java application.
- Tests should live under `src/test/java` and use the repository's Maven/JUnit 4 conventions.
- The repository may need Java 8 for `mvn test` because Lombok 1.16.22 can fail under newer JDKs.

## Coverage Automation Notes

- Focus coverage additions on regression-prone behavior: login/session handling, interceptor access control, mapper XML SQL invariants, prescription/dispensing validation, date formatting, and form parsing.
- Keep test changes deterministic and independent; avoid UI snapshots and environment-dependent integration tests.
- 2026-10-08: Added deterministic JUnit 4 tests for `WebConfig`, `LoginInterceptor`, `PatientServiceImpl.seek`, and `SeekMapper.xml`; full `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 5 tests.
