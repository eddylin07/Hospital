# MEMORY.md

## Project facts

- Project name: Hospital.
- Stack: Spring Boot 2.0, MyBatis, Freemarker, Maven.
- Purpose: medical information management system.

## Environment and testing

- Use Java 8 for Maven validation because Lombok 1.16.22 fails with newer javac module access rules.
- Preferred test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Some cloud images may require installing Maven and `openjdk-8-jdk-headless` before tests can run.

## Coverage automation notes

- Add focused JUnit 4/Mockito tests for risky behavior instead of broad Spring context tests when possible.
- Mapper XML behavior can be tested by parsing the XML with MyBatis APIs, avoiding database dependencies.
- 2026-08-19 coverage run on `cursor/missing-test-coverage-d1b6` added tests for failed/successful login session handling, login interceptor redirect/allow behavior, and `SeekMapper.updateDrugs` latest-visit/null-safe price SQL; validation passed with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
