# AGENTS.md

## Project

- Name: Hospital
- Root: `/workspace`
- Date initialized: 2026-07-21

## Stack

- Java Maven project
- Spring Boot 2.0.0.RELEASE
- MyBatis / MyBatis Generator
- FreeMarker templates
- MySQL connector
- LayUI-based frontend resources

## Working rules

- Read `AGENTS.md` and `MEMORY.md` before changing project files.
- Keep fixes minimal and aligned with existing Spring MVC/MyBatis patterns.
- Prefer focused tests for critical authorization, persistence, and inventory/accounting paths.
- Do not store credential values in memory files; record locations only when needed.
- Use Maven for validation when available.
