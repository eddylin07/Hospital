# AGENTS.md

## Project

- Name: Hospital
- Type: Java web application
- Stack: Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, Layui, MySQL

## Working Guidelines

- Follow existing package layout under `src/main/java/com/hospital`.
- Keep tests deterministic and scoped to behavior with regression risk.
- Prefer the repository's current Maven/Spring Boot test conventions.
- Do not commit secrets or environment-specific credentials.

## Validation

- Run the most relevant Maven test target for changed test areas.
- If full project tests are not practical because of local environment dependencies, document the blocker and run the narrowest reliable target.
