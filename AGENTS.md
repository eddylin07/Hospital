# Agent Guidelines

## Project
- Name: Hospital
- Stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker.
- Main application entry: `src/main/java/com/hospital/HospitalApp.java`.

## Working Rules
- Read `MEMORY.md` before making code changes.
- Follow existing Java package and test conventions.
- Prefer deterministic tests that do not require external databases or services.
- Keep production changes minimal; for coverage work, prefer tests over behavior changes.

## Validation
- Use Maven for Java validation.
- Run the narrowest relevant test target first, then broader Maven tests when feasible.
- Record any environment-dependent failures in the final summary.
