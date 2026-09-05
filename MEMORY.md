# Project Memory

## Stable facts

- Project: Hospital (`com.wxthxy.hospital:HospitalAction`)
- Stack: Java, Spring Boot 2.0, Maven, MyBatis, FreeMarker, MySQL
- Tests should follow JUnit 4 and Spring Boot starter test conventions already available through Maven.
- Validation command known to work in prior automation runs: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`

## Environment notes

- The template directory `/home/ubuntu/.codex/templates` was unavailable when `AGENTS.md` and this file were initialized.
- The repository may need Maven and JDK 8 installed before tests can run because Lombok 1.16.22 is incompatible with modern default javac module restrictions.

## Coverage automation notes

- Prior coverage runs found high-value regression risks around login session handling, interceptor access control, prescription/dispensing inventory updates, mapper XML SQL invariants, and date formatting.
- On branch `cursor/missing-test-coverage-5c45`, tests were added for latest-seek-only/null-safe prescription SQL, dispensing inventory/price validation, failed/successful login session handling, and calendar-year date formatting; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 7 tests.
