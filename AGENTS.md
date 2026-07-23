# AGENTS.md

## Project

- Name: Hospital
- Repository root: `/workspace`
- Default work branch for this run: `cursor/critical-bug-investigation-89bc`

## Stack

- Java Maven application
- Spring Boot 2.0.0.RELEASE
- MyBatis / MyBatis Generator
- Freemarker templates
- Layui / jQuery frontend assets
- MySQL connector and Druid datasource

## Working rules

- Read this file and `MEMORY.md` before making code or state changes.
- Keep changes scoped to the requested bug investigation or fix.
- Prefer existing controller/service/mapper patterns over new abstractions.
- For behavioral fixes, add focused tests when practical.
- Commit and push completed code changes to the designated branch.

## Unknowns

- Local database credentials and runtime dataset are not documented here.
