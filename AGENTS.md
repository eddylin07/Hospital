# Agent Instructions

## Project

- Project name: Hospital
- Stack: Spring Boot 2.0, MyBatis, Freemarker, Maven, Java
- Domain: hospital/medical information management

## Working conventions

- Follow existing package structure under `com.hospital`.
- Keep production behavior changes narrowly scoped and only make them when tests expose a real regression risk.
- Prefer focused unit or mapper XML tests over broad application context tests unless the behavior requires Spring integration.
- Do not commit credentials or environment-specific secrets. Record credential locations only, never values.

## Testing

- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- The project uses old Lombok (`1.16.22`); Java 8 is required for reliable Maven test runs in this environment.
- If Maven or JDK 8 is unavailable on a fresh image, install `maven` and `openjdk-8-jdk-headless` before validation.
