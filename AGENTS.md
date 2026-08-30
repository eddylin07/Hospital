# AGENTS.md

## Project

- Name: HospitalAction
- Repository root: `/workspace`
- Stack: Java, Spring Boot 2.0, MyBatis, Freemarker, Maven, MySQL

## Agent workflow

- Read this file and `MEMORY.md` before making code changes.
- Keep changes focused on the requested task.
- For critical bug investigations, prefer small, high-confidence fixes with targeted tests.
- Do not commit temporary debugging instrumentation.

## Testing

- Use Maven tests when changing Java code.
- If Maven is unavailable in the cloud image, install it with the system package manager before running tests.
- For GUI evidence, create walkthrough artifacts under `/opt/cursor/artifacts` when code changes are delivered.
