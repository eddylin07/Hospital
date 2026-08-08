# Project Memory

## Repository facts

- Hospital is a Spring Boot 2.0/Maven/MyBatis hospital management app.
- Main Java package: `com.hospital`.
- MyBatis SQL lives in `src/main/resources/mapper`.
- Fresh branches may start with no tests in `src/test/java`.

## Validation facts

- The project uses Lombok 1.16.22, which fails with the runner default Java 21 compiler module access.
- Validate with JDK 8:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- Some cloud images may need `maven` and `openjdk-8-jdk-headless` installed before tests can run.

## Coverage automation notes

- High-value prior coverage targets include authentication session handling, login interceptor behavior, prescription/dispensing inventory updates, mapper XML result mappings, and parsing helpers.
- Prefer small JUnit 4/Mockito unit tests and XML parsing tests over database-dependent tests unless a live database is required.
- Historical risky invariants:
  - Failed login must not store an authenticated user in the session.
  - Anonymous requests should be redirected by `LoginInterceptor`; authenticated requests should pass.
  - Dispensing should not update inventory or prescriptions for insufficient or non-positive requested quantities.
  - Prescription SQL should only update the latest seek row and should accumulate price from null safely.

## Current run notes

- On branch `cursor/missing-test-coverage-2f93`, `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` SQL so drug updates target only the latest seek row and null prices are accumulated with `ifnull(price,0)`.
- Validation passed with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` (2 tests).
