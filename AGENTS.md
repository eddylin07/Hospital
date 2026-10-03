# AGENTS.md

## Project

- Name: Hospital
- Root: `/workspace`
- Date initialized: 2026-10-03

## Stack

- Java Spring Boot 2.0.0.RELEASE
- MyBatis / PageHelper
- Maven
- Freemarker templates
- MySQL connector
- JUnit 4 through `spring-boot-starter-test`

## Working rules

- Read `MEMORY.md` before changing code.
- Prefer focused regression tests over broad snapshot tests.
- Follow existing Maven/Spring Boot conventions.
- Use JDK 8 for builds/tests because Lombok 1.16.22 is incompatible with modern javac module boundaries.
- Recommended validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

