# Project Instructions

- Project: Hospital
- Created: 2026-08-12
- Stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, MySQL.
- Test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` when JDK 8 is available. Lombok 1.16.22 is not compatible with the default Java 21 compiler.
- Follow existing package layout under `src/main/java/com/hospital` and place tests under matching `src/test/java/com/hospital` packages.
- Keep tests deterministic and independent; prefer focused JUnit 4/Mockito tests or mapper XML parsing tests over broad Spring context tests unless the behavior requires integration coverage.
