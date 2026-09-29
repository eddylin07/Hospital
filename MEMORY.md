# Project Memory

## Project facts

- Project name: Hospital.
- Root: `/workspace`.
- Stack inferred on 2026-09-29: Maven Java application using Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, MySQL connector, and `spring-boot-starter-test`.
- The repository did not contain `AGENTS.md` or `MEMORY.md` at session start; template files were also not present under `~/.codex/templates/`, so minimal project-local docs were created from inference.

## Testing notes

- Prefer focused Maven test runs for touched packages/classes.
- Full Spring/database integration tests may need confirmation of local database availability.
- On 2026-09-29, `SeekMapperXmlTest` was added to parse the real MyBatis XML and protect `SeekMapper.updateDrugs`: prescription updates must only affect the latest seek row for a patient and must add charges with `ifnull(price,0)`. Validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Open questions

- Supported JDK version.
- Required local database setup for full integration tests.
- Preferred test naming/package conventions if maintainers use a non-default pattern.
