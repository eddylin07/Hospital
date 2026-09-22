# AGENTS.md

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven
- Main package: `com.hospital`

## Working guidelines

- Keep production changes minimal and tied to tested behavior.
- Follow existing Java package structure under `src/main/java` and `src/test/java`.
- Prefer focused unit or mapper XML tests over broad Spring context tests when possible.
- Treat credentials in configuration as local development placeholders; do not copy secret values into notes or external output.

## Validation

- This project uses old Lombok and compiles reliably on JDK 8.
- Preferred command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- If Maven or JDK 8 is absent in a fresh environment, install them before validation.
