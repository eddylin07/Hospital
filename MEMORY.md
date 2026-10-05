# Project Memory

## Confirmed Facts

- Date initialized: 2026-10-05.
- Repository: Hospital.
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven.
- Test environment: Maven validation should run with Java 8 because Lombok 1.16.22 fails on newer JDKs.
- The template directory `/home/ubuntu/.codex/templates` was unavailable during initialization, so these project-local instruction files were created from inferred repository facts.

## Coverage Automation Notes

- Prior automation runs often added focused JUnit 4 / Mockito tests for login/session handling, interceptor access control, dispensing/inventory logic, mapper XML SQL invariants, and shared date utilities.
- Prefer tests that cover business invariants with meaningful regression risk over broad snapshot-style checks.
- 2026-10-05 coverage run on branch `cursor/missing-test-coverage-27e8`: added `SeekMapperXmlTest` for `updateDrugs` and fixed the SQL so prescription dispensing updates only the latest seek row while treating existing NULL price as zero. Validation: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed.
