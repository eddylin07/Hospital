# Agent Instructions

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven, MySQL
- Current development branch: `cursor/critical-bug-investigation-98ce`

## Working Rules

- Read `MEMORY.md` before making project changes.
- Keep fixes minimal and focused on confirmed high-severity correctness/security bugs.
- Prefer existing controller/service/mapper patterns over introducing new architecture.
- Before testing, verify whether the cloud async setup is still running.
- Commit and push completed changes to `cursor/critical-bug-investigation-98ce`.

## Validation Notes

- Maven may be missing in the cloud image; install it before running `mvn test` if needed.
- The project may need Java 21 compatibility updates for Lombok/Surefire before tests can run.
