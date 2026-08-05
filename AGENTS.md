# AGENTS.md

## Project

- Name: Hospital
- Inferred date: 2026-08-05
- Stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL, Layui-style frontend templates.

## Working rules

- Read `AGENTS.md` and `MEMORY.md` before changing project files.
- Keep changes small and focused on the requested task.
- Prefer existing project patterns over introducing new framework choices.
- For bug fixes, describe user impact, root cause, fix, and validation.

## Testing

- Use Maven tests/build checks when code changes are made.
- Add focused tests when the touched code has a practical test seam.

## Memory

- Record durable architecture decisions, pitfalls, and user corrections in `MEMORY.md`.
- Never store credential values; only record their locations if needed.
