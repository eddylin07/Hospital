# Project Agent Instructions

## Project

- Name: Hospital / HospitalAction
- Type: Spring Boot 2.0.0.RELEASE medical information management system.
- Primary stack: Java, Maven, Spring Boot, MyBatis, FreeMarker, MySQL.

## Development Guidelines

- Follow existing package structure under `src/main/java/com/hospital`.
- Keep changes small and behavior-focused.
- Prefer existing test conventions and fixtures when adding coverage.
- Do not record credential values in documentation or memory files.

## Testing

- Use Maven for Java tests.
- This project depends on Lombok 1.16.22 and should be tested with JDK 8:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh environment, install them before validation.

## Notes

- `AGENTS.md` and `MEMORY.md` were created from inferred project facts because `/home/ubuntu/.codex/templates` was not available in this environment.
