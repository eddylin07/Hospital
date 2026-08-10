# Project Memory

## Confirmed project facts

- Project name: Hospital.
- Repository path: `/workspace`.
- Technology stack: Java Maven application using Spring Boot 2.0.0, MyBatis, FreeMarker, MySQL connector, PageHelper, FastJSON, POI, and iText.
- Main application class: `src/main/java/com/hospital/HospitalApp.java`.
- As of 2026-08-10, no Java tests were present under `src/test/java`.

## Testing notes

- Use `mvn test` as the primary automated test command.
- Prefer focused unit tests for utility classes and edge-case logic to avoid requiring a database or full Spring context.

## Working notes

- Credentials: record locations only, never values.
- Code-discoverable facts should stay in code or AGENTS.md rather than being duplicated here.
