# Project Instructions

- Project: Hospital
- Last updated: 2026-08-23
- Stack: Java, Spring Boot 2.0, Maven, MyBatis, Freemarker, Layui, MySQL.

## Working Guidelines

- Prefer small, high-confidence changes that match the existing Spring MVC/MyBatis style.
- For bug-finding automation, only fix issues with a concrete trigger and severe impact: data loss, crashes, security holes, or significant user-facing breakage.
- Add focused tests for behavioral fixes when practical.
- Do not store credentials in repository files; document only where credentials are expected to be configured.

## Validation

- Use targeted unit tests for narrow service/controller behavior.
- Run `mvn test` when Maven is available and code changes affect Java behavior.
