# Project Memory

## Project Facts

- Project name: Hospital.
- Repository path: `/workspace`.
- Java Spring Boot 2.0.0.RELEASE + MyBatis + Freemarker Maven application.
- Root templates directory `/home/ubuntu/.codex/templates` was unavailable on 2026-08-06, so `AGENTS.md` and `MEMORY.md` were created from inferred project context.

## Validation

- Maven tests should be run with Java 8 when possible:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Fresh cloud images may need Maven and JDK 8 installed before validation.

## Coverage Automation Notes

- Focus coverage additions on risky business behavior: authentication/session handling, request interceptors, dispensing inventory/price updates, mapper SQL invariants, and form parsing utilities.
- Keep tests minimal, deterministic, and aligned with existing JUnit 4 / Mockito conventions.
