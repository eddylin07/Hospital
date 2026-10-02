# AGENTS.md

## Project

- Name: Hospital
- Type: Java Maven web application
- Stack: Spring Boot 2.0, MyBatis, FreeMarker, MySQL, Layui

## Working conventions

- Default to small, high-confidence changes.
- Prefer focused tests for controller/service/interceptor behavior when changing critical paths.
- Run `mvn test` after Java changes when Maven is available.
- Do not store credentials in this file or in MEMORY.md; record credential locations only.

## Known environment notes

- Some Cursor Cloud images may not include Maven. Install Maven before running tests if `mvn` is missing.
- The project historically targets an older Java/Spring Boot/Lombok stack, so Java 21 compatibility may require dependency updates.
