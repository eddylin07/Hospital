# AGENTS.md

## Project

- Name: Hospital
- Type: Java Maven web application
- Stack: Spring Boot 2.0, MyBatis, Freemarker/Layui, MySQL

## Working rules

- Follow repository-local patterns and keep fixes minimal.
- For bug-finding automation, prioritize critical correctness and security issues: authentication/authorization bypasses, data loss/corruption, crashes in critical flows, race conditions, and silent truncation.
- Do not record credential values. `src/main/resources/application.yml` may contain credential fields; record only file paths or configuration shape.

## Validation

- Prefer focused unit tests for critical fixes when possible.
- Run `mvn test` for Java changes. If Maven is unavailable in the cloud image, install it before testing.
