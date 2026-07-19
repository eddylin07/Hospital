# Agent Instructions

## Project

- Name: Hospital
- Type: Java/Spring Boot medical information management system.
- Stack inferred from repository: Spring Boot 2.0.0, MyBatis, Freemarker, MySQL, Maven, Lombok.

## Working rules

- Read `MEMORY.md` before changing code.
- Keep fixes minimal and aligned with the existing controller/service/mapper structure.
- For security-sensitive paths, prefer server-side authorization checks over UI-only restrictions.
- Validate critical fixes with focused tests when feasible.
- Do not record secret values in repository files; record only their locations if needed.

## Known local constraints

- The original dependency set may not compile on Java 21 without dependency updates.
- Maven may be absent in the cloud image; install Maven before running tests if needed.
