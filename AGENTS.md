# Agent Instructions

## Project

- Name: Hospital
- Type: Maven Java web application
- Stack: Spring Boot 2.0.0, MyBatis, Freemarker, Layui, MySQL

## Working Rules

- Follow the existing Maven/Spring/MyBatis structure.
- Keep production edits scoped and minimal.
- Prefer focused JUnit tests for behavior with regression risk.
- Use JDK 8 for Maven validation because the project uses Lombok 1.16.22.
- Do not store credentials in this file or in MEMORY.md.

## Validation

- Preferred test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing, install them before validation.
