# MEMORY.md

## Project Facts

- Project name: Hospital.
- Repository root: `/workspace`.
- Application stack inferred on 2026-09-23: Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, Layui, MySQL.
- Test coverage automation should prioritize meaningful regression tests and run the narrowest reliable Maven target for touched areas.

## Notes

- `~/.codex/templates/AGENTS.md` and `~/.codex/templates/MEMORY.md` were not present in this environment, so initial files were created from inferred project context.
- On 2026-09-23, `SeekMapperXmlTest` was added to parse `SeekMapper.xml` without a database and guard `updateDrugs` so dispensing updates only the latest seek row and accumulates price with `ifnull(price,0)`.
- Validation on 2026-09-23: `mvn -Dtest=SeekMapperXmlTest test` and full `mvn test` passed under Java 8/Maven 3.8.7.
