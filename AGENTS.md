# Agent Instructions

Project: Hospital
Date initialized: 2026-10-16

## Project overview

- Java Maven web application for hospital/medical information management.
- Main stack inferred from the repository: Spring Boot 2.0.0, MyBatis, FreeMarker, Layui, MySQL.
- Entry point: `src/main/java/com/hospital/HospitalApp.java`.

## Working rules

- Read `AGENTS.md` and `MEMORY.md` before making project code changes.
- Keep code changes minimal and focused on the requested task.
- Prefer existing project patterns over introducing new frameworks or broad refactors.
- For bug fixes, include a concrete trigger scenario, the impact, and validation evidence.
- Do not record credentials or secret values in repository files or memory.

## Common commands

- Compile/package: `mvn test` or `mvn package` depending on task scope.
- Application config: `src/main/resources/application.yml`.
