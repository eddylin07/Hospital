# Project Memory

## Stable facts

- Hospital is a Maven-based Spring Boot 2/MyBatis hospital management application.
- The repository may start without project-local agent files; templates were unavailable under `/home/ubuntu/.codex/templates` on 2026-09-06.
- Maven validation should run with JDK 8: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Coverage automation notes

- Prior coverage runs repeatedly found meaningful risk around login session handling, interceptor access checks, prescription dispensing, mapper XML SQL, and date formatting near year boundaries.
- Prefer focused tests around business invariants: no session on failed login, authenticated-only protected routes, all-or-nothing drug dispensing, latest-visit-only prescription updates, null-safe price accumulation, mapper result mappings, and calendar-year date formatting.
- 2026-09-06 on `cursor/missing-test-coverage-a2a5`: added `SeekMapperXmlTest` for `updateDrugs` latest-seek-only and null-safe price accumulation; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 1 test.

## Open questions

- None for the current baseline.
