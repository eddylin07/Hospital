# AGENTS.md

## Project

- Name: Hospital
- Stack: Java Spring Boot 2.0, Maven, MyBatis, FreeMarker, MySQL.

## Working rules

- Read `MEMORY.md` before making code changes.
- Keep fixes minimal and focused; avoid broad refactors during bug-fix automation runs.
- Do not store credentials in repository files. Record only credential locations if needed.

## Build and test

- Use Maven for Java build/test workflows.
- If `mvn` is unavailable in the cloud image, install Maven before running tests.
- Prefer targeted tests for narrow fixes, then run the broader Maven test command when practical.

