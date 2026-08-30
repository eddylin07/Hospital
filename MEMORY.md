# MEMORY.md

## Project facts

- Project name: Hospital.
- Stack: Java Spring Boot 2.0, Maven, MyBatis, FreeMarker, MySQL.
- Maven tests should be run with JDK 8: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Notes

- `/home/ubuntu/.codex/templates` was unavailable when this file and `AGENTS.md` were created, so project-local minimal guidance was inferred from `pom.xml` and prior automation memory.
- 2026-08-30 coverage run on `cursor/missing-test-coverage-2cc8`: `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so dispensing updates only the latest seek row and treats existing NULL price as zero; validation passed with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Record durable architecture decisions, recurring environment issues, and testing pitfalls here. Do not record secret values.
