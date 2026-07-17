# MEMORY.md

## Project Facts

- Project name: Hospital
- Date initialized: 2026-07-17
- Stack inferred from repository: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, Layui.
- Maven artifact: `com.wxthxy.hospital:HospitalAction`.

## Environment Notes

- `~/.codex/templates/AGENTS.md` and `~/.codex/templates/MEMORY.md` were not present in this cloud image, so project-local minimal files were created from inferred repository facts.
- Use Java 8 for Maven validation because Lombok 1.16.22 can fail under newer JDKs.

## Testing Notes

- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

