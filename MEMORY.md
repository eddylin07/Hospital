# Project Memory

## Confirmed facts

- Created: 2026-08-05.
- Project name: Hospital.
- Stack: Spring Boot 2.0.0.RELEASE, MyBatis, Maven, Freemarker, MySQL connector.
- Build/test note: Lombok 1.16.22 requires Java 8 for reliable Maven test runs in this environment.
- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Coverage automation notes

- Prior coverage runs found high-value regressions around login session handling, interceptor access control, prescription dispensing, mapper XML SQL, and drug form parsing.
- Favor focused JUnit 4/Mockito tests and MyBatis XML parsing tests that do not require a live database.
- On branch `cursor/missing-test-coverage-e2e7`, added focused tests for `LoginInterceptor`, `PatientServiceImpl.seek`, and `DrugsUtils`; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 5 tests.
