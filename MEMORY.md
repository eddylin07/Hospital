# MEMORY.md

## Project Facts

- Project name: Hospital.
- Repository path in this environment: `/workspace`.
- Stack inferred from repository: Java Spring Boot with MyBatis and Maven.
- Project-local templates were not available at `/home/ubuntu/.codex/templates/`, so this `MEMORY.md` and `AGENTS.md` were initialized from inferred repository facts on 2026-09-03.

## Testing Notes

- Use Maven for Java tests.
- Prior automation memory reports Lombok 1.16.22 may not compile under Java 21; prefer `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` when JDK 8 is installed.
- Some cloud images may need Maven and JDK 8 installed before validation.

## Coverage Automation Notes

- Recent automation runs focused on high-risk coverage for login session handling, login interceptor authorization redirects, patient dispensing inventory/price behavior, mapper XML SQL invariants, and date formatting edge cases.
- Keep future coverage additions deterministic, narrow, and aligned with existing JUnit/Mockito patterns once tests exist in the branch.
- On branch `cursor/missing-test-coverage-52b2`, `SeekMapperXmlTest` covers the latest `SeekMapper.updateDrugs` SQL so prescriptions only update the newest seek row and null existing prices remain billable via `ifnull(price,0)`.
