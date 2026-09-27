# Project Memory

## Project Facts

- Hospital is a Maven-based Java medical information management system.
- The application uses Spring Boot 2.0, MyBatis, Freemarker, Layui, and MySQL.
- Test work should follow existing Maven conventions and prefer focused regression tests over low-signal snapshots.

## Open Notes

- `AGENTS.md` and `MEMORY.md` were initialized because the workspace did not contain them and no templates were available under `~/.codex/templates/`.
- This run added `SeekMapperXmlTest` for `SeekMapper.updateDrugs` and validated with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
