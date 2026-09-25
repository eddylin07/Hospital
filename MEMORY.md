# MEMORY.md

## Project facts

- Project name: Hospital.
- Initialized for agent workflow on 2026-09-25.
- Detected stack: Java Maven application using Spring Boot 2.0, MyBatis, Freemarker templates, and Layui assets.

## Testing notes

- Prefer focused Maven tests near modified production code.
- Add regression tests only where behavior or business risk justifies coverage.
- On branch `cursor/missing-test-coverage-94ff`, `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so dispensing updates only the latest seek row and null existing prices are billable with `ifnull(price,0)`. Validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn -Dtest=SeekMapperXmlTest test`.

