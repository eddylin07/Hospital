# Project Agent Instructions

## Project

- Name: Hospital
- Type: Spring Boot 2 / MyBatis / Freemarker medical information management system
- Date initialized: 2026-08-28

## Development guidance

- Follow existing Java package, Maven, MyBatis XML, and JUnit 4 test conventions.
- Prefer focused tests for business-risk paths such as authentication, prescription updates, inventory changes, date formatting, and mapper SQL.
- Keep production changes minimal; do not refactor unrelated code during coverage automation.

## Validation

- Use Maven for automated tests.
- This project uses Lombok 1.16.22; prefer JDK 8 when running Maven tests:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
