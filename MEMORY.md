# MEMORY.md

## Project facts

- Project name: Hospital
- Date created: 2026-08-17
- Stack: Java, Spring Boot 2.0.0, Maven, MyBatis, Freemarker
- Build descriptor: `pom.xml`

## Environment notes

- `/home/ubuntu/.codex/templates` was unavailable when project-local `AGENTS.md` and `MEMORY.md` were first required, so minimal files were created from inferred project details.
- Lombok 1.16.22 is incompatible with the runner default Java 21 compiler. Use JDK 8 for Maven validation.
- Known validation command:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

## Coverage automation notes

- Prior coverage runs for this repository focused on login/session handling, interceptor authentication checks, prescription dispensing inventory and price updates, mapper XML invariants, and date formatting around calendar-year boundaries.
- On branch `cursor/missing-test-coverage-9182`, `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so prescription dispensing updates only the latest seek row and treats existing NULL price as zero. Validation: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 2 tests.
