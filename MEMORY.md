# MEMORY.md

## Project Notes

- Project name: Hospital.
- Date initialized: 2026-07-31.
- The app is a Spring Boot 2/MyBatis medical information management system built with Maven.
- Use Java 8 for Maven validation because the checked-in Lombok 1.16.22 is not compatible with newer javac module access defaults.
- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
- Prior automation runs found high-risk areas around login session handling, interceptor authorization, patient drug dispensing, and MyBatis mapper XML for prescriptions/drug fields.
- On branch `cursor/missing-test-coverage-e05f`, tests were added for login/session authorization, login interceptor behavior, patient drug dispensing stock/price writes, and mapper XML prescription/drug-field invariants.
- Validation passed on this branch with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`: 9 tests, 0 failures, 0 errors, 0 skipped.

## Memory Update Policy

- Record architecture decisions, validation pitfalls, and user corrections here when they are not already obvious from source code.
- Do not store credential values; store only locations or operational notes.

