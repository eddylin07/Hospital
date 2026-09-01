# Agent Instructions

## Project

- Project name: Hospital
- Stack: Java, Spring Boot 2.0, MyBatis, FreeMarker, Maven, MySQL
- Purpose: medical/hospital information management system.

## Development Notes

- Follow existing package structure under `src/main/java/com/hospital`.
- Keep changes narrowly scoped and prefer existing controller/service/mapper patterns.
- Do not commit credentials or database secrets. `src/main/resources/application.yml` may reference local configuration; record credential locations only, never values.
- The project may require Java 8 because Lombok 1.16.22 is not compatible with modern javac module boundaries.

## Testing

- Prefer deterministic JUnit 4 / Spring Boot test conventions from `spring-boot-starter-test`.
- For this repository, the known validation command is:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If Maven or JDK 8 is unavailable on a fresh cloud image, install `maven` and `openjdk-8-jdk-headless` before running tests.

