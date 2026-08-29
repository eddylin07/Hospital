# Project Agent Instructions

## Project

- Name: HospitalAction (repository: Hospital)
- Stack: Java, Spring Boot 2.0.0.RELEASE, MyBatis, Maven
- Package root: `com.wxthxy.hospital`

## Build and Test

- Use Maven for Java validation.
- The project uses Lombok 1.16.22, so prefer JDK 8 when compiling or running tests:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- Follow existing JUnit 4 and Mockito-style tests when adding coverage.

## Working Notes

- Keep tests deterministic and independent.
- Prefer focused unit or mapper XML tests over broad context-loading tests unless the changed behavior requires Spring integration.
- Do not record credentials or secret values in repository files.
