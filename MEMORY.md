# MEMORY.md

## Project facts

- Hospital is a Spring Boot 2.0.0.RELEASE Maven application using MyBatis XML mappers, FreeMarker templates, MySQL, and Lombok 1.16.22.
- Tests should avoid requiring a live database; existing automation patterns favor JUnit 4, Mockito, and direct MyBatis XML parsing.
- Maven validation should run with Java 8: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Coverage automation notes

- Recurrent high-risk areas are login/session handling, authentication interceptor behavior, patient dispensing/inventory updates, and MyBatis prescription SQL.
- Prior automation memory notes that some fresh images require installing Maven and OpenJDK 8 before validation.
- On branch `cursor/missing-test-coverage-cc53`, regression tests cover failed-login session handling, interceptor anonymous redirect/no-session behavior, dispensing price/inventory validation, latest-seek-only/null-safe prescription SQL, drug mapper price/number/text mapping, and empty drug/option form parsing. `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` passed with 12 tests after installing Maven/JDK 8.

## Credentials

- No project credentials are documented here. Store only credential locations, never secret values.
