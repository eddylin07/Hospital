# MEMORY.md

## Project facts

- Project name: Hospital.
- The repository is a Maven Java application using Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker templates, Lombok, JUnit 4, and Mockito.
- Source code lives under `src/main/java/com/hospital`; MyBatis mapper XML files live under `src/main/resources/mapper`.
- Tests should follow the existing Maven layout under `src/test/java`.

## Build and test

- Use Java 8 for validation: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Lombok 1.16.22 can fail on newer JDKs, so avoid validating with Java 17/21 unless the build is updated.

## Coverage automation notes

- Prior coverage runs repeatedly found risk around login session handling, interceptor authentication gates, prescription/dispensing inventory logic, mapper XML update scope, null-safe price accumulation, and drug form parsing.
- Keep tests minimal and behavior-focused; only change production code when a test exposes a real regression risk.

## External resource locations

- Persistent automation memory is available through the Cursor Automation Tools MCP server.
