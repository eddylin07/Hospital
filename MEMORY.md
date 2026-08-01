# Project Memory

Initialized: 2026-08-01

## Project facts

- Repository directory: `/workspace`
- Project name: Hospital
- Build system: Maven
- Primary stack: Java, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL
- Test dependency is available through `spring-boot-starter-test`.
- `~/.codex/templates` was not present on 2026-08-01, so `AGENTS.md` and `MEMORY.md` were initialized directly from inferred project facts.

## Validation notes

- Use focused Maven test runs for touched areas.
- If Lombok fails under a modern JDK, install/use JDK 8 and run Maven with:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`

## Coverage automation notes

- Prefer tests for business behavior with regression risk: authentication/session handling, mapper SQL, parsing utilities, inventory/dispensing, permissions, and data validation.
- Avoid database-dependent tests unless the project already provides stable fixtures.
- On branch `cursor/missing-test-coverage-7a69`, coverage was added for login session handling, interceptor allow/redirect behavior, dispensing stock/price updates, insufficient-stock no-write behavior, latest-seek-only/null-safe prescription SQL, and drug price/number/text result mapping.
- `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 8 tests on 2026-08-01.
