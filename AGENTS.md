# AGENTS.md

## Project

- Name: Hospital
- Type: Medical information management system
- Stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven, MySQL
- Date initialized: 2026-09-02

## Working rules

- Read this file and `MEMORY.md` before modifying files.
- Keep fixes minimal and focused on the requested task.
- Prefer existing controller/service/mapper patterns over new abstractions.
- Do not record secret values. Credential locations may be noted in `MEMORY.md`.

## Testing

- For backend changes, prefer focused unit tests plus `mvn test` when dependencies are available.
- If Maven is unavailable, install it in the cloud environment before relying on test results.
