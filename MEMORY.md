# Project Memory

## Project Facts

- Project name: Hospital.
- Application stack: Spring Boot 2.0.0.RELEASE, Maven, MyBatis, Freemarker, Lombok 1.16.22.
- Source root: `src/main/java/com/hospital`.
- Templates live under `src/main/resources/templates`.
- Mapper XML files live under `src/main/resources/mapper`.
- Tests should live under `src/test/java` and follow JUnit 4 / Mockito conventions.

## Validation Notes

- Lombok 1.16.22 is not compatible with modern JDK module defaults. Prefer:
  `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- Some cloud images may need Maven and OpenJDK 8 installed before tests can run.

## Coverage Priorities

- High-value regression tests in this repository usually cover controller session behavior, interceptor authentication behavior, service validation around prescriptions/inventory, mapper XML SQL invariants, and shared form parsing utilities.
- Keep tests deterministic and avoid depending on a live database or web server unless strictly necessary.
