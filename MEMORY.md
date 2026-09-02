# MEMORY.md

## Project facts

- Project name: Hospital.
- Initialized on 2026-09-02 because the workspace did not include AGENTS.md or MEMORY.md and the template directory was unavailable.
- README describes the project as a medical information management system using SSM, Layui, and Freemarker.
- `pom.xml` shows a Java Maven application based on Spring Boot 2.0.0.RELEASE with MyBatis, Freemarker, MySQL, Druid, Lombok, Apache POI, and iText.

## Operating notes

- Read `AGENTS.md` and this file before file modifications.
- `src/main/resources/application.yml` may contain credential fields; remember only the path, never values.
- If tests are needed and Maven is missing, install Maven before running `mvn test`.

## Current session notes

- 2026-09-02: Initialized AGENTS.md and MEMORY.md from inferred project facts because templates were unavailable.
- 2026-09-02: Fixed critical auth/session, role authorization, public admin self-registration, patient appointment IDOR, doctor workflow patient authorization, prescription inventory overdraw/race/partial-update, latest-seek-row prescription updates, empty input crashes, missing appointment/seek PDF crashes, MyBatis multi-parameter binding, long prescription SQL truncation, Linux-friendly PDF output paths, and public hospital content page allowlisting. Validation: `mvn test` passed with 19 tests on Java 8 / Maven 3.8.7.
