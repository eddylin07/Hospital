# AGENTS.md

## Project

- Name: Hospital
- Type: Java web application for hospital/medical information management
- Stack: Spring Boot 2.0, Maven, MyBatis, FreeMarker, MySQL, Layui

## Working guidelines

- Default to Chinese for user-facing summaries; keep code, commands, and identifiers in English.
- Read `MEMORY.md` before making code changes and update it when learning durable project facts.
- Do not record credential values. If credentials are relevant, record only the file path that contains them.
- Keep fixes minimal and focused, especially for security or data-integrity bugs.

## Testing notes

- Use `mvn test` for automated validation when Maven is available.
- This legacy Spring Boot/Lombok stack may require dependency updates to run on modern Java versions.
