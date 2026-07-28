# Agent Instructions

## Project
- Name: workspace
- Repository role: Hospital Spring Boot application
- Primary stack: Java, Maven, Spring Boot 2.0, MyBatis, Freemarker
- Generated on: 2026-07-28

## Working rules
- Read `AGENTS.md` and `MEMORY.md` before modifying the project.
- Keep changes scoped to the requested behavior and follow existing package/style conventions.
- Prefer focused tests over broad, low-signal snapshots.
- Do not store credentials or secret values in this repository.

## Validation notes
- This project uses old dependencies including Lombok 1.16.22. On modern runners, Maven tests may require JDK 8.
- Preferred validation command when JDK 8 is available:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
