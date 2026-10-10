# AGENTS.md

## Project
- Name: Hospital
- Description: Medical information management system.
- Stack: Java, Spring Boot 2.0.0, MyBatis, Freemarker, Maven, MySQL.

## Working rules
- Read `MEMORY.md` before making repository changes.
- Keep fixes minimal and scoped to the requested bug or feature.
- Prefer existing controller/service/mapper patterns over introducing new abstractions.
- Do not commit generated build output or local environment files.

## Validation
- Use Maven for build/test validation when dependencies and environment are available.
- For controller/security changes, inspect the full request path: route mapping, interceptor configuration, session state, service calls, and templates.
