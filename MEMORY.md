# MEMORY.md

## Project facts

- Project name: Hospital.
- Stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker.
- Domain: medical information management system.

## Validation

- Use JDK 8 for Maven validation because Lombok 1.16.22 fails with newer javac module access rules.
- Known successful command:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

## Coverage focus

- High-value regression targets include login/session handling, request interceptor behavior, patient dispensing logic, inventory and billing validation, mapper XML SQL shape, and date formatting around calendar boundaries.

## History

- Created on 2026-10-01 because the workspace did not include `AGENTS.md` or `MEMORY.md`, and `~/.codex/templates/` did not provide templates.
- On branch `cursor/missing-test-coverage-d075`, `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so dispensing updates only the latest seek row and null existing prices remain billable; `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 1 test.
