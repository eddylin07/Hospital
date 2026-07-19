# MEMORY.md

## Project facts

- Project name: Hospital
- Stack: Spring Boot 2.0.0.RELEASE, MyBatis, Maven, Java 8-era dependencies.
- The repository initially has no `src/test` tree on this branch.

## Validation

- Use `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Lombok 1.16.22 is incompatible with the default Java 21 compiler module access.

## Automation notes

- `~/.codex/templates/AGENTS.md` and `~/.codex/templates/MEMORY.md` were not present in this cloud image, so minimal project-local files were created from inferred project details.
