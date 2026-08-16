# Project Memory

## Project facts

- Project: HospitalAction in `/workspace`.
- Stack inferred from `pom.xml`: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker.
- `AGENTS.md` and `MEMORY.md` were missing on 2026-08-16; templates were also unavailable at `~/.codex/templates/`, so minimal project-local files were created from inspection.

## Testing notes

- Maven test support is present via `spring-boot-starter-test`.
- Use `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`; Java 8 is required because Lombok 1.16.22 is incompatible with newer JDK module access.
- On branch `cursor/missing-test-coverage-21bf`, regression tests cover failed-login session handling, all-or-nothing dispensing stock/price updates, latest-seek-only/null-safe prescription SQL, and `DrugsMapper` dispensing field mapping. The command above passed with 6 tests on 2026-08-16.

## Open questions

- None yet.
