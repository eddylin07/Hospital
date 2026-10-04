# Repository Instructions

## Project

- Name: Hospital
- Stack: Java 8, Spring Boot 2.0, MyBatis, Freemarker, Maven
- Purpose: Medical information management system.

## Build and test

- Use Maven for Java validation.
- Lombok 1.16.22 requires Java 8 on current runners; run tests with:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh cloud image, install `maven` and `openjdk-8-jdk-headless`.

## Testing guidance

- Follow existing JUnit 4 / Spring Boot test conventions.
- Prefer focused unit or mapper XML tests over broad fragile UI/integration tests.
- Keep tests deterministic and independent of a live database unless the target already requires one.
