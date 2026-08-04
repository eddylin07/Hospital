# MEMORY.md

## Project facts

- Project name: Hospital.
- Initialized on: 2026-08-04.
- Stack: Spring Boot 2.0.0, MyBatis, Maven, FreeMarker, MySQL, Lombok 1.16.22.
- Package root: `com.hospital`.

## Testing

- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- JDK 8 is preferred because Lombok 1.16.22 can fail under newer Java versions.
- At initialization, no `src/test` tree was checked in on this branch.

## Notes

- `/home/ubuntu/.codex/templates/` was unavailable in this environment, so `AGENTS.md` and `MEMORY.md` were created from inferred project details.
