# Project Memory

## Facts

- Project name: Hospital.
- Repository appears to be a Maven Java web application using Spring Boot 2.0, MyBatis, Freemarker, and a MySQL schema under `sql/`.
- Date initialized: 2026-10-15.

## Testing Notes

- Maven includes `spring-boot-starter-test`; inspect existing tests before adding new ones.
- The template directory `/home/ubuntu/.codex/templates/` was unavailable on 2026-10-15, so project-local `AGENTS.md` and `MEMORY.md` were initialized manually.
- Java 8 and Maven are available in this run; `mvn test` passes with the current test suite.
- `src/test/java/com/hospital/mapper/SeekMapperXmlTest.java` covers `SeekMapper.updateDrugs` so prescription dispensing updates only the patient's latest seek row and treats a NULL existing price as zero.

## Open Questions

- No user-provided project-specific testing policy beyond the generated `AGENTS.md` baseline yet.
