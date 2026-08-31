# Project Memory

## Confirmed Project Facts

- `/workspace` is the Hospital Java application using Spring Boot 2.0, MyBatis, and Maven.
- Lombok 1.16.22 fails on newer JDKs in this environment; run tests with JDK 8:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing, install `maven` and `openjdk-8-jdk-headless`.

## Test Coverage Focus

- High-risk areas previously identified in this repository include login/session handling, request interception, prescription dispensing, MyBatis mapper SQL, drug form parsing, and shared date/PDF formatting helpers.
- Prefer focused JUnit 4 / Spring Boot test patterns already present or introduced under `src/test/java`.
- 2026-08-31: Added `MapperXmlTest` to parse MyBatis mapper XML without a database and guard prescription SQL plus drug field mapping. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 2 tests.
