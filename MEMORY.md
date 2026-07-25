# Project Memory

## Project Facts

- Hospital is a Maven Java medical information management system.
- Core stack inferred from `pom.xml`: Spring Boot 2.0.0, MyBatis, Freemarker, MySQL, Lombok 1.16.22.
- The README describes the project as a medical information management system using SSM, Layui, and Freemarker.

## Environment Notes

- `~/.codex/templates/AGENTS.md` and `~/.codex/templates/MEMORY.md` were not present in this cloud image, so project-local files were initialized with a minimal structure.
- Use `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` for validation because Lombok 1.16.22 is incompatible with newer javac module boundaries.

## Coverage Automation Notes

- Prior automation memory indicates this repository often lacks checked-in tests and has recurring risk around login session handling, patient dispensing, and `SeekMapper.updateDrugs` SQL.
