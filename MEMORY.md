# Project Memory

## Project Facts
- Project name: Hospital.
- Detected on 2026-10-16.
- Stack: Maven Java project using Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL connector, and Spring Boot test support.
- Source layout follows standard Maven paths under `src/main/java` and `src/main/resources`.

## Testing Notes
- Prefer tests that avoid a real database unless a local fixture is already present.
- Start with focused unit tests for utility or controller logic when recent changes touch high-risk behavior.
- The local templates `/home/ubuntu/.codex/templates/AGENTS.md` and `MEMORY.md` were unavailable, so project-local starter files were created directly.
- Validation works with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- `src/test/java/com/hospital/mapper/SeekMapperXmlTest.java` parses `SeekMapper.xml` without a database and protects `updateDrugs` so dispensing updates only the latest seek row and handles null existing prices with `ifnull(price,0)`.

## Open Questions
- None yet.
