# Project Memory

## Environment

- Date initialized: 2026-09-04.
- Project root: `/workspace`.
- Project identity inferred from `README.md` and `pom.xml`: Hospital / HospitalAction.
- Tech stack inferred from the repository: Spring Boot 2, MyBatis, Maven, FreeMarker, MySQL.

## Testing

- Use JDK 8 for Maven tests because Lombok 1.16.22 is not compatible with modern javac module access defaults.
- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Automation Notes

- `/home/ubuntu/.codex/templates` was unavailable when this file and `AGENTS.md` were initialized, so both were created from inferred project facts.
- 2026-09-04 coverage run on `cursor/missing-test-coverage-dfed`: added JUnit 4 coverage for failed/successful login session handling, all-or-nothing dispensing stock/price updates, latest-seek-only/null-safe prescription SQL, and calendar-year date formatting. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 7 tests.
