# AGENTS.md

## Project

- Name: Hospital
- Stack: Java, Spring Boot 2.0.0, Maven, MyBatis, Freemarker
- Default branch: master
- Automation branch pattern: cursor/missing-test-coverage-*

## Local setup

- The project uses Lombok 1.16.22 and should be compiled with JDK 8.
- Use Maven for tests.
- Preferred validation command:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

## Working rules

- Follow existing Spring, MyBatis, JUnit 4, and Mockito patterns.
- Keep tests deterministic and independent.
- Add focused tests for business-risk behavior rather than cosmetic snapshots.
- Do not change production behavior unless a minimal fix is required to make the tested invariant true.
- If Maven or JDK 8 is missing in a fresh environment, install them before running tests.

## Memory

- Read `MEMORY.md` before making project changes.
- Update `MEMORY.md` when learning durable project facts, test commands, or environment caveats.
