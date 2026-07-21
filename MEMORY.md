# Project Memory

## Project Facts

- Hospital is a Java Maven application using Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, and Lombok 1.16.22.
- Use JDK 8 for Maven validation because Lombok 1.16.22 fails on newer JDKs.
- Relevant validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Testing Conventions

- Place tests in `src/test/java` using package paths matching production code.
- Prefer JUnit 4 and Mockito-based unit tests for service/controller/interceptor logic.
- Avoid tests that require a live database; mapper XML behavior can be tested by parsing MyBatis XML when needed.

## Coverage Automation Notes

- Prior coverage work has targeted `SeekMapper.updateDrugs`, `PatientServiceImpl.seek`, `LoginInterceptor`, and `DrugsUtils`.
- `LoginControllerTest` covers login session handling: failed authentication must not create a `login` session attribute, while successful authentication stores the authenticated `Login`.
- Keep new tests focused on risky business logic, validation, parsing, permissions, or shared utilities.
