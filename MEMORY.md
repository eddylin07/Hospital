# MEMORY.md

## Project facts

- Project name: HospitalAction
- Created/updated: 2026-07-22
- Stack: Java, Maven, Spring Boot 2.0, MyBatis, Freemarker, Lombok 1.16.22

## Validation notes

- Use Java 8 for Maven tests: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Lombok 1.16.22 can fail with newer JDKs because of javac module access restrictions.

## Automation notes

- `/home/ubuntu/.codex/templates` was not present in this environment, so AGENTS.md and MEMORY.md were bootstrapped locally from inferred project metadata.
