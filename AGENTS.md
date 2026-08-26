# Project Instructions

## Project

- Name: Hospital
- Type: Java Spring Boot 2.0.0 / MyBatis / Freemarker hospital information management system.
- Build tool: Maven (`pom.xml`).
- Date initialized: 2026-08-26.

## Development Notes

- Prefer small, behavior-focused changes that match the existing controller/service/mapper structure.
- Keep tests deterministic and close to the changed code path.
- Do not introduce new dependencies unless they are necessary for the requested behavior.
- Existing Lombok version is old; use JDK 8 when compiling or running tests.

## Testing

- Standard validation command:

  ```bash
  JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
  ```

- If Maven or JDK 8 is unavailable in a fresh environment, install them before validation:

  ```bash
  sudo apt-get update
  sudo apt-get install -y maven openjdk-8-jdk-headless
  ```

