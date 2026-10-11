# MEMORY.md

## Project Facts

- Project name: Hospital.
- Repository path: `/workspace`.
- Technology stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL.
- Template source `/home/ubuntu/.codex/templates` was unavailable when `AGENTS.md` and `MEMORY.md` were initialized on 2026-10-11.

## Testing Notes

- Prefer `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` for validation when JDK 8 is installed.
- Existing automation memory says old Lombok can fail on newer JDKs, so verify Java version before relying on Maven test results.
- On 2026-10-11, `SeekMapperXmlTest` was added to cover `SeekMapper.updateDrugs` SQL: update only the latest seek row for a patient and use `ifnull(price,0)` for null-safe price accumulation. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed.
