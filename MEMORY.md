# MEMORY.md

## Project memory

- Project: Hospital (`/workspace`), a Spring Boot 2/MyBatis medical information management system.
- Local templates for `AGENTS.md` and `MEMORY.md` were not present at `/home/ubuntu/.codex/templates`; project-local files were created from inferred repository context.
- Maven validation may need Java 8 because Lombok 1.16.22 can fail under newer JDK module restrictions.
- Known validation command from prior automation runs: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Test coverage focus

- Prefer focused regression tests around business-critical mapper SQL, service validation, authentication/session behavior, and shared utility logic.
- Existing automation memory indicates prior useful tests covered prescription SQL (`SeekMapper.updateDrugs`), `PatientServiceImpl.seek`, login session behavior, and `DrugsUtils`.

## Session notes

- 2026-07-24: Initialized local project instruction and memory files because both were missing and the template directory was unavailable.
- 2026-07-24: Added regression coverage for failed-login session handling, drug dispensing stock/price behavior, and latest-seek-only prescription SQL; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 5 tests.
