# MEMORY.md

## Project facts

- Hospital is a Java Spring Boot 2 / MyBatis Maven application.
- The repository has historically had little or no checked-in test coverage on `master`.
- The app currently depends on Lombok 1.16.22, so Maven validation should run with JDK 8 rather than the default modern JDK.

## Validation

- Preferred command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- Fresh cloud images may need Maven and OpenJDK 8 installed before tests can run.

## Coverage focus

- High-risk areas seen in prior coverage work: login/session handling, interceptor authorization behavior, dispensing inventory and billing logic, mapper XML SQL invariants, and shared date/formatting utilities.
- This run adds regression coverage for `PatientServiceImpl.seek` dispensing validation and `SeekMapper.updateDrugs` SQL invariants. It also fixes dispensing to validate all requested drug quantities before writes and fixes the mapper to update only the latest seek row with null-safe price accumulation. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 4 tests.
- Follow-up coverage from the exploration subagent added `LoginControllerTest` for failed/successful login session behavior and fixed `LoginController.login` to store the session only when the login service returns a success message. The same Maven command passed with 6 tests.
