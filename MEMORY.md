# MEMORY.md

## Project facts

- Project name: Hospital.
- Inferred stack: Java, Spring Boot 2.0.0, MyBatis, Freemarker, Maven, MySQL.
- Repository root contains `pom.xml`, `src/`, `sql/`, and `gener.xml`.
- README describes the project as a medical information management system.

## Operational notes

- Created on 2026-08-08 because the workspace did not contain AGENTS.md or MEMORY.md and the configured template directory was unavailable.
- Use `mvn test` for validation when dependencies are available.
- If Maven is missing in this cloud image, install it before running tests.

## Review focus

- Critical bug automation should prioritize concrete data loss, crashes, security holes, auth bypasses, inventory/write races, and high-impact user-facing breakage.
