# Project Memory

## Project Facts

- Repository: Hospital
- Application: hospital information management system.
- Stack: Spring Boot 2.0.0, MyBatis, Freemarker, Maven, JUnit 4/Spring Boot test.
- `gener.xml` contains MyBatis Generator database connection settings; do not copy credential values into memory.

## Test and Build Notes

- The project uses Lombok 1.16.22, which can fail to compile on newer JDKs. Prefer JDK 8 for Maven validation.
- Known validation command:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- Some fresh cloud images may need Maven and JDK 8 installed before tests can run.

## Coverage Automation Notes

- The original `master` branch had little or no checked-in test coverage.
- Prior automation work added reference JUnit 4/Mockito style tests for MyBatis XML and service/interceptor behavior.
- Existing high-risk areas include MyBatis mapper SQL, login/session handling, patient visit creation, and utility logic.

