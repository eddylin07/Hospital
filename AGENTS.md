# Agent Instructions

## Project

- Name: Hospital
- Type: Spring Boot 2.0 / MyBatis Maven application for hospital information management.
- Main source: `src/main/java/com/hospital`
- MyBatis mapper XML: `src/main/resources/mapper`
- Static/Freemarker UI assets: `src/main/resources/static` and `src/main/resources/templates`

## Setup and testing

- Use Maven for Java builds and tests.
- Lombok 1.16.22 is not compatible with the default Java 21 compiler. Prefer JDK 8 when running tests:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh environment, install `maven` and `openjdk-8-jdk-headless`.

## Coding guidance

- Follow existing Spring MVC service/controller/mapper patterns.
- Keep tests deterministic and independent; prefer focused unit or mapper XML tests for regression coverage.
- Avoid broad refactors when adding coverage. Only change production code when a test exposes a real bug.

## Last updated

- 2026-08-24
