# Project Memory

## Environment

- Hospital is a Spring Boot 2/MyBatis Maven application.
- Lombok 1.16.22 does not compile reliably on newer JDKs; use JDK 8 for Maven validation:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

## Testing conventions

- Prefer JUnit 4 and Mockito, matching `spring-boot-starter-test` from Spring Boot 2.0.
- Keep tests independent of MySQL by mocking mappers/services or parsing mapper XML directly for SQL invariants.
- High-value regression areas observed in this project: login session handling, `LoginInterceptor`, dispensing/prescription stock and price logic, mapper XML update scopes, and calendar-year date formatting.
- On branch `cursor/missing-test-coverage-7f5f`, tests were added for login session handling, dispensing inventory validation, latest-seek prescription SQL, null-safe prescription price accumulation, and calendar-year date formatting. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 7 tests.

## Open questions

- None currently; project name, stack, and validation command are inferred from the repository.
