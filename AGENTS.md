# AGENTS.md

## Project

- Name: Hospital
- Type: medical information management system
- Stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Layui, MySQL, Maven
- Main application resources: `src/main/resources`
- Java source: `src/main/java`

## Working guidelines

- Keep changes narrowly scoped to the requested behavior.
- Prefer existing service, mapper, and controller patterns over broad rewrites.
- Treat authentication, authorization, appointment ownership, prescription inventory, and PDF/export paths as high-risk areas.
- Do not store credential values in memory files; record only where configuration lives.

## Testing

- Use Maven for automated tests when available: `mvn test`.
- If Maven is unavailable in the environment, install it before test execution.
- For Java 21 environments, this project may need updated Lombok/Surefire versions before tests can run.
