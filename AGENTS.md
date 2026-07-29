# AGENTS.md

## Project

- Name: Hospital
- Type: Java web application
- Stack: Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, Maven, MySQL
- Generated/domain code lives under `src/main/java/com/hospital`.

## Working rules

- Read `MEMORY.md` before making code or test changes.
- Keep changes scoped to the requested behavior and follow existing package layout.
- Prefer focused regression tests over broad snapshots or cosmetic assertions.
- Do not commit credentials or environment-specific secrets.

## Build and test

- Use Maven for validation.
- This project uses Lombok 1.16.22, which is incompatible with the default Java 21 compiler.
- Prefer:

```sh
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

## Test conventions

- Put tests under `src/test/java` using package names that mirror production packages.
- Spring Boot 2 test dependencies provide JUnit 4 and Mockito; follow those conventions unless the project changes.
- Keep tests deterministic and independent from a live database where possible.
