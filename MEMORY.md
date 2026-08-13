# Project Memory

## Known Environment

- Project: Hospital.
- Updated: 2026-08-13.
- Stack: Java Spring Boot 2, MyBatis, Maven.
- Validation generally requires JDK 8 because the checked-in Lombok version does not compile cleanly on newer Java versions.
- Preferred test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Testing Priorities

- High-risk existing flows include login session handling, interceptor access control, patient drug dispensing, mapper XML SQL, and shared date/PDF utilities.
- Existing automation history repeatedly found meaningful regressions around latest-visit prescription updates, null-safe price accumulation, inventory bounds, and failed-login session persistence.
- On branch `cursor/missing-test-coverage-f396`, `LoginControllerTest`, `LoginInterceptorTest`, `SeekMapperXmlTest`, and `DateUtilsTest` cover failed-login session handling, anonymous/authenticated interceptor behavior, latest-seek-only/null-safe prescription SQL, and calendar-year date formatting. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 7 tests.
