# Project Memory

## Environment

- `/workspace` is the Hospital Spring Boot 2/MyBatis/Freemarker Maven application.
- Validate tests with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Lombok 1.16.22 is checked in and can fail on newer Java versions; use JDK 8 for compilation.
- Some cloud images may require installing Maven and OpenJDK 8 before running tests.

## Repository Notes

- No project-local `AGENTS.md` or `MEMORY.md` existed when this run started.
- `/home/ubuntu/.codex/templates` was unavailable, so local minimal instruction and memory files were created from observed repository facts.
- Database connection settings live in `src/main/resources/application.yml`; do not copy credential values into memory.
- On branch `cursor/missing-test-coverage-c1b0`, `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so prescription updates target only the latest seek row and null prices remain billable with `ifnull(price,0)`. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 1 test.
