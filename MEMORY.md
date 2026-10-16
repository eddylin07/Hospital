# Project Memory

## Project facts

- Repository name: Hospital.
- Initialized memory on 2026-10-16 because the workspace did not contain `AGENTS.md` or `MEMORY.md`.
- Stack inferred from files: Java Maven, Spring Boot 2.0.0, MyBatis mapper XML, FreeMarker templates, Layui static frontend, MySQL schema under `sql/hospital.sql`.

## Operating notes

- No credential values are stored here; record locations only when needed.
- Prefer focused fixes with tests or at least a Maven compile/test validation.
- On 2026-10-16, critical fixes covered failed-login session persistence, server-side role authorization, public admin self-registration/duplicate username binding, patient appointment IDOR, and transactional guarded dispensing that updates only the latest seek row.
