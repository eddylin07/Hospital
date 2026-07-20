# Project Memory

## Known Setup

- Hospital is a Spring Boot 2.0/MyBatis/Freemarker Maven application.
- The project uses Lombok 1.16.22; Maven compilation should run with Java 8, not the runner default Java 21.
- Known successful validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Fresh cloud images may need `maven` and `openjdk-8-jdk-headless` installed before tests can run.

## Test Coverage Notes

- The historical `master` branch had little or no checked-in tests.
- Useful regression targets from prior automation runs:
  - `SeekMapper.updateDrugs` SQL should update only the latest seek row and handle null price accumulation.
  - `PatientServiceImpl.seek`, `LoginInterceptor`, and `DrugsUtils` have had focused JUnit 4 / Mockito tests on prior coverage branches.
- Keep new tests deterministic and independent of a live database by parsing mapper XML or mocking DAO collaborators.

## Credentials

- No project credentials are recorded here. If credentials are needed, record only their location, not their values.
