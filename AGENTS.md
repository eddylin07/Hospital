# AGENTS.md

## Project

- Name: Hospital
- Repository root: `/workspace`
- Stack: Java, Spring Boot 2.0.0.RELEASE, MyBatis, Maven, Freemarker
- Domain: hospital/medical information management system

## Working rules for agents

- Read this file and `MEMORY.md` before making code or external-state changes.
- Follow existing Maven/Spring/MyBatis conventions and keep changes narrowly scoped.
- Prefer tests that cover business behavior over low-signal snapshots or cosmetic assertions.
- Do not store secrets in this repository; if a credential location is needed, document only the location.

## Validation

- Use JDK 8 for this project because Lombok 1.16.22 is not compatible with modern javac module access.
- Preferred test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- If Maven or JDK 8 are missing in a fresh runner, install them before validation.
