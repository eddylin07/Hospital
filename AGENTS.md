# AGENTS.md

## Project

- Name: Hospital
- Type: Java/Spring Boot hospital information management system
- Build tool: Maven
- Main stack: Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, MySQL, Layui

## Working Guidelines

- Read `MEMORY.md` before changing code.
- Follow existing package structure under `src/main/java/com/hospital`.
- Keep tests deterministic and scoped to the behavior being changed.
- Prefer Maven test targets for validation.
- Do not store credentials or secret values in repository files.

## Test Guidance

- Add tests under `src/test/java` using the existing Maven/Spring Boot test dependency set.
- For utility classes and logic with no container dependency, prefer focused unit tests over full Spring context tests.
- Run the smallest relevant Maven test target first; run broader validation when changes touch shared behavior.
