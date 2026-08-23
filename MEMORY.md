# Project Memory

## Project facts

- Created: 2026-08-23
- Project: Hospital, a Spring Boot 2/MyBatis/Freemarker hospital information management app.
- Build system: Maven.
- Reliable test command in this environment: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Testing notes

- Use Java 8 for Maven tests because the checked-in Lombok version is not compatible with newer javac module boundaries.
- Favor deterministic tests around mapper XML, service invariants, login/session handling, and shared utility parsing/formatting.

## Automation notes

- This workspace initially had no project-local `AGENTS.md` or `MEMORY.md`; `/home/ubuntu/.codex/templates` did not contain the requested templates, so local files were created from inferred project facts.
- 2026-08-23 coverage run: added focused tests for login session writes, patient prescription dispensing, and `SeekMapper.updateDrugs`; validation passed with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` (5 tests).
