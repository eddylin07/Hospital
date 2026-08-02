# Project Instructions

## Project

- Name: workspace / HospitalAction
- Type: Spring Boot 2.0.0.RELEASE hospital management web application
- Main stack: Java, Maven, Spring Boot MVC, MyBatis, FreeMarker, MySQL

## Working Rules

- Follow existing Maven/Spring/MyBatis conventions.
- Keep tests deterministic and focused on behavior with business risk.
- Prefer JUnit 4 and Mockito through `spring-boot-starter-test`, matching the Spring Boot 2 dependency set.
- Avoid production behavior changes unless a failing high-value test exposes a real regression risk.
- Use Java 8 for validation because the pinned Lombok 1.16.22 dependency is not compatible with modern javac module access.

## Validation

- Preferred full test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If Maven or JDK 8 is missing on a fresh runner, install them before validation.
