# Project Instructions

## Project

- Name: Hospital
- Stack: Spring Boot 2.0, MyBatis, Freemarker, Maven, JUnit 4/Spring Boot test.
- Domain: hospital/medical information management.

## Development Notes

- Keep changes focused on behavior and regression risk.
- Follow existing Java package layout under `src/main/java/com/hospital` and `src/test/java/com/hospital`.
- Prefer deterministic unit or mapper XML tests over broad UI or database-dependent tests.
- Do not commit secrets or environment-specific credentials.

## Testing

- Lombok 1.16.22 is not compatible with modern JDK module defaults; use JDK 8 for Maven validation.
- Preferred validation command:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing in a fresh environment, install them before running tests.
