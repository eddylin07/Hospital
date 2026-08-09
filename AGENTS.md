# Agent Instructions

## Project

- Name: Hospital
- Stack: Java Spring Boot 2, MyBatis, Maven, Freemarker templates
- Default branch for this automation: `cursor/missing-test-coverage-0599`

## Development

- Follow the existing package layout under `src/main/java/com/hospital`.
- Keep behavior changes minimal and focused on the tested risk.
- Prefer existing JUnit 4 and Mockito conventions for tests.

## Validation

- Use JDK 8 for Maven because Lombok 1.16.22 is not compatible with modern javac module access.
- Preferred command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`

## Notes

- Project-local `AGENTS.md` and `MEMORY.md` were created because `/home/ubuntu/.codex/templates` was unavailable in this cloud environment.
