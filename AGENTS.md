# Agent Instructions

## Project

- Name: Hospital
- Repository: https://github.com/eddylin07/Hospital
- Stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker.

## Working Guidelines

- Follow existing Java package structure under `src/main/java/com/hospital`.
- Add tests under matching packages in `src/test/java`.
- Prefer focused JUnit 4 tests and Mockito where dependencies should be isolated.
- Keep tests deterministic and avoid requiring a live database or web server.

## Validation

- This project uses Lombok 1.16.22, which is not compatible with modern JDK module defaults.
- Prefer JDK 8 for Maven test runs:
  - `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`

## Notes

- Do not record credentials in this file.
- Keep production behavior changes out of coverage work unless a small bug fix is necessary to make the tested behavior correct.
