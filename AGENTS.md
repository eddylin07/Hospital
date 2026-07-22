# Agent Instructions

## Project

- Name: Hospital
- Type: Java web application for hospital information management.
- Stack: Spring Boot 2.0, MyBatis, FreeMarker, Layui, Maven, MySQL.

## Working Rules

- Prefer minimal, high-confidence fixes for correctness and security issues.
- Preserve existing Spring MVC/MyBatis patterns unless a local invariant requires a tighter change.
- For auth/session/role checks, enforce behavior server-side; UI hiding is not authorization.
- For inventory or prescription writes, protect against stale reads and concurrent overdraw with database-side guards.
- Keep credentials out of memory notes and commit messages; record only their file locations when needed.

## Validation

- Use Maven for Java tests when available: `mvn test`.
- The cloud image may lack Maven; if so, install it with the system package manager before running tests.
- This legacy project may need dependency updates to compile on modern JDKs.
