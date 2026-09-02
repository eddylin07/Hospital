# Project Memory

## Repository Facts

- Hospital is a Maven-based Java Spring Boot 2 / MyBatis medical information management system.
- The project uses Lombok 1.16.22; Maven test runs should use JDK 8.
- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- `/home/ubuntu/.codex/templates` was unavailable when `AGENTS.md` and `MEMORY.md` were initialized, so project-local files were created from inferred repository facts.

## Testing Notes

- Favor focused JUnit 4 and Mockito tests for service/controller/interceptor logic.
- Mapper SQL behavior can be covered by parsing MyBatis XML in unit tests when no database is needed.
- 2026-09-02 coverage run on branch `cursor/missing-test-coverage-06a1` added `SeekMapperXmlTest` and `LoginInterceptorTest`; `SeekMapper.updateDrugs` now uses `ifnull(price,0)`, targets the latest seek row with `order by id desc limit 1`, and full `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 3 tests.

## Credentials

- No credentials recorded. Store only credential locations if they are ever needed.
