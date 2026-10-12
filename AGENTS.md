# AGENTS.md

## Project

- Name: Hospital
- Inferred stack: Java, Maven, Spring Boot 2.0.0, MyBatis, Freemarker, MySQL
- Date initialized: 2026-10-12

## Working rules

- Follow existing package layout and test conventions.
- Keep tests deterministic and independent of external services when possible.
- Prefer focused automated tests for business logic and controller behavior.
- Do not store credentials or secret values in this file.

## Verification

- Use Maven for Java validation.
- Run the narrowest relevant test target first; run broader checks when the change has shared impact.
