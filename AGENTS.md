# AGENTS.md

## Project

- Name: Hospital
- Type: Spring Boot 2 / MyBatis medical information management system
- Build tool: Maven
- Primary language: Java

## Environment

- Prefer JDK 8 for build and test commands because the checked-in Lombok version is not compatible with modern javac module access.
- Known validation command:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

## Working rules

- Follow existing Spring MVC, service, mapper, and XML mapper patterns.
- Keep tests deterministic and focused on business-risk behavior.
- Avoid cosmetic-only tests and broad refactors unrelated to the requested change.
- Do not store credential values in this file or in `MEMORY.md`; only record their locations when needed.
