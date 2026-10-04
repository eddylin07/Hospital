# Project Memory

## Project facts

- Project: Hospital.
- Created: 2026-10-04.
- Stack: Java 8, Spring Boot 2.0, MyBatis, Freemarker, Maven.
- Test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Operational notes

- Lombok 1.16.22 is not compatible with the default Java 21 compiler on the runner; use JDK 8 for Maven test runs.
- Existing automation history shows useful regression targets around login session handling, interceptor access control, patient dispensing logic, MyBatis mapper XML, and shared date formatting.
- 2026-10-04 coverage run: `SeekMapperXmlTest` covers `SeekMapper.updateDrugs` so dispensing updates only the latest seek row and treats an existing NULL price as zero; validation passed with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.
