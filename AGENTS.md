# Agent Instructions

## Project

- Name: Hospital
- Main artifact: HospitalAction
- Tech stack: Java, Spring Boot 2.0.0.RELEASE, Maven, MyBatis, MySQL, Freemarker.

## Working Guidelines

- Read `MEMORY.md` at the start of work and update it with durable findings that are not obvious from code.
- Keep changes minimal and focused on the requested task.
- Prefer existing Spring Boot/MyBatis project patterns over new abstractions.
- For code changes, run targeted tests when available; otherwise run Maven compile/test commands that are practical for the change.

## Common Commands

- Compile: `mvn -q -DskipTests compile`
- Test: `mvn test`
