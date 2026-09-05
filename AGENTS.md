# AGENTS.md

## Project

- Name: Hospital
- Type: medical information management system
- Stack: Java, Spring Boot/SSM-style MVC, MyBatis, FreeMarker, Layui
- Database assets: `sql/hospital.sql`

## Working rules

- Read `MEMORY.md` before modifying project files.
- Keep fixes minimal and focused on the requested issue.
- For high-severity bug investigation, only open a PR when there is a concrete trigger scenario and a high-confidence fix.
- Prefer existing service/controller/mapper patterns over broad refactors.

## Validation

- When business code changes, run focused tests when available and then `mvn test` when the environment supports it.
- Maven may be absent in the cloud image; install it if required before running tests.
