# MEMORY.md

## Project facts

- Project name: Hospital.
- Repository appears to be a Maven Java web application.
- README describes it as a medical information management system using SSM, Layui, and Freemarker.
- `pom.xml` uses Spring Boot 2.0.0.RELEASE, MyBatis, PageHelper, Freemarker, MySQL connector, Apache POI, and iText.

## Environment notes

- On 2026-08-27, `AGENTS.md` and `MEMORY.md` were missing from the project root.
- The expected template directory `~/.codex/templates/` was not present, so minimal project-local versions were initialized from repository inspection.

## Testing notes

- Prefer focused Maven test targets for modified Java areas; use `mvn test` when practical.
- `SeekMapperXmlTest` parses `src/main/resources/mapper/SeekMapper.xml` directly with MyBatis `XMLMapperBuilder` and validates generated BoundSql for `updateDrugs`.
- On branch `cursor/missing-test-coverage-f95e`, `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 2 mapper XML tests after fixing `SeekMapper.updateDrugs` to update only the latest seek row and use null-safe price accumulation.
