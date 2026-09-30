# Project Memory

## Confirmed Context

- Project name: Hospital.
- Date initialized: 2026-09-30.
- Stack inferred from `pom.xml` and repository layout: Java, Spring Boot 2.0, Maven, MyBatis, FreeMarker, MySQL.
- The project currently has production code under `src/main/java/com/hospital` and should place tests under `src/test/java`.

## Testing Notes

- Prefer focused Maven tests for shared utilities and edge-case logic that do not require a live MySQL database.
- Use `mvn -Dtest=ClassName test` for focused validation when adding a small number of tests.
- 2026-09-30: Added deterministic JUnit4/Mockito tests for login session handling, login interceptor allow/redirect behavior, dispensing inventory/price validation, and `SeekMapper.updateDrugs` SQL. Full validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Open Items

- No project-specific CI command beyond Maven has been confirmed.
