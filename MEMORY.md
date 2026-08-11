# MEMORY.md

## Project facts

- Project name: Hospital.
- Stack: Java, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, Maven.
- Test command in this environment: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Lombok 1.16.22 requires JDK 8 for compilation here; Java 21 fails on javac module access.

## Coverage automation notes

- Prior runs found high-value regression surfaces in login/session handling, request interceptor behavior, prescription dispensing, MyBatis mapper XML, date formatting, and form parsing utilities.
- Keep new tests deterministic and independent; prefer testing service and mapper invariants directly instead of adding UI snapshots.
- On branch `cursor/missing-test-coverage-c447`, `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so dispensing updates only the latest seek row and treats existing `NULL` prices as zero. Validation: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 1 test.

## Open questions

- No project-specific template files were available at `/home/ubuntu/.codex/templates` during setup.

