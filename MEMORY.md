# MEMORY.md

## Project facts

- Hospital is a Spring Boot 2/MyBatis Maven application for hospital information management.
- The app currently uses legacy Lombok 1.16.22; run tests with JDK 8 via `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Template files under `/home/ubuntu/.codex/templates/` were unavailable when this project memory was created, so this file and `AGENTS.md` were initialized from inferred repository facts.

## Coverage automation notes

- Prior coverage analysis identified high-risk behavior around login session handling, auth interception, dispensing inventory/price updates, and MyBatis prescription SQL.
- Useful minimal test targets in this codebase include controller/service unit tests with JUnit 4 + Mockito and XML parsing tests for mapper SQL.
- When adding regression tests, keep them deterministic and independent of a live database.
- On branch `cursor/missing-test-coverage-bbed`, regression tests cover login session writes, login interceptor behavior, dispensing inventory/price updates, insufficient-stock no-write behavior, mapper SQL latest-seek/null-safe price updates, and drug price/number result mapping. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 8 tests after installing Maven/JDK 8.
