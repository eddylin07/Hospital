# Project Memory

## Project facts

- Project name: Hospital.
- Stack identified on 2026-10-09: Java Maven, Spring Boot 2.0.0.RELEASE, MyBatis/PageHelper, Freemarker, MySQL, Spring Boot test.
- No repository-provided AGENTS.md or MEMORY.md existed at session start; template files were also unavailable under `/home/ubuntu/.codex/templates/`, so minimal project files were created.

## Testing notes

- Prefer focused Maven tests for touched Java areas.
- Avoid tests that require a live MySQL instance unless the project already provides a deterministic fixture or embedded database setup.
- 2026-10-09: Added JUnit4/Mockito tests for `PatientServiceImpl.seek` happy-path dispensing side effects and `LoginInterceptor.preHandle` anonymous/authenticated behavior; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 3 tests.

