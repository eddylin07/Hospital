# MEMORY.md

## Project facts

- Project name: Hospital.
- Detected stack: Java Maven application using Spring Boot 2.0.0, MyBatis, Freemarker, and JUnit/Spring Boot test support.
- Initial repository memory created on 2026-10-14 because no project-level `MEMORY.md` template was available at `~/.codex/templates/`.

## Testing notes

- Prefer focused Maven test runs for modified test classes or packages.
- Add tests only where they cover meaningful regression risk in business logic, parsing, validation, permissions, concurrency, or shared utilities.
- 2026-10-14: `SeekMapperXmlTest` parses `mapper/SeekMapper.xml` without a database and validates the active `updateDrugs` SQL plus its drugs/price/patientid parameter binding. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed.

## Open questions

- None currently blocking automated coverage work.
