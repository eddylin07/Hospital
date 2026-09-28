# Project Memory

## Project Facts

- Project name: Hospital.
- Date initialized: 2026-09-28.
- Stack: Java Spring Boot 2.0, MyBatis, FreeMarker, Maven.
- README describes the app as a medical information management system.

## Testing Notes

- Use Maven test targets for validation.
- Prefer focused tests for risky utility, parsing, validation, permission, and core flow behavior.
- 2026-09-28 coverage automation added JUnit4/Mockito/XML parser tests for login session handling, interceptor anonymous behavior, dispensing validation/all-or-nothing writes, mapper prescription SQL/result mappings, and empty drug/option form parsing.
- Validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
