# Agent Instructions

## Project

- Project name: Hospital
- Stack: Spring Boot 2.x, MyBatis, Maven, FreeMarker templates, JUnit 4/Mockito tests.
- Default branch: master
- Automation branch pattern: `cursor/missing-test-coverage-*`

## Environment

- Use JDK 8 for Maven validation because the repository uses Lombok 1.16.22, which is incompatible with modern javac module access.
- Known validation command:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If Maven or JDK 8 is unavailable in a fresh cloud image, install `maven` and `openjdk-8-jdk-headless`.

## Working Rules

- Follow existing production and test style.
- Keep tests deterministic and independent.
- Prefer focused unit or XML parsing tests for risky controller, service, utility, and mapper behavior.
- Do not change production behavior unless a small fix is required to make an important invariant true.
- Do not record secret values in docs or memory.
