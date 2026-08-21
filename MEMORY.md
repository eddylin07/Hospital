# Project Memory

## Facts

- Project name: Hospital.
- Stack: Java Spring Boot 2.0.0, MyBatis, FreeMarker, Maven.
- Maven artifact: `com.wxthxy.hospital:HospitalAction`.
- The repository root did not contain `AGENTS.md` or `MEMORY.md` on 2026-08-21.
- `/home/ubuntu/.codex/templates` was unavailable in this environment, so project-local files were initialized from inferred repository facts.

## Environment

- Lombok 1.16.22 is incompatible with the default Java 21 compiler module access.
- Use JDK 8 for Maven tests when available:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`

## Testing guidance

- Prior coverage automation runs successfully used JUnit 4 and Mockito-style focused unit/XML mapper tests.
- High-risk areas seen in prior runs include login session handling, interceptor authorization behavior, prescription dispensing inventory and price updates, mapper XML result mappings, and date formatting around week-year boundaries.
- On branch `cursor/missing-test-coverage-4e33`, `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so prescription dispensing updates only the latest seek row, treats existing NULL price as zero, and keeps mapper parameter binding stable. Validation: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 3 tests.
