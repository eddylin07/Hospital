# Agent Instructions

## Project

- Name: Hospital
- Stack: Java, Maven, Spring Boot 2.x, MyBatis, FreeMarker, MySQL.
- Date initialized: 2026-10-07.

## Working Guidelines

- Follow existing Maven/Spring Boot project conventions.
- Prefer focused, deterministic tests for risky behavior over broad snapshot tests.
- Keep production changes minimal; do not change behavior unless needed to make meaningful tests possible.
- Use `mvn test` or narrower Maven test targets for validation when changing tests.

