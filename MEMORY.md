# Project Memory

## Project Facts

- HospitalAction is a Java Spring Boot 2.0.0.RELEASE and MyBatis Maven application.
- The project uses Lombok 1.16.22, so validation should run with Java 8 rather than the default modern JDK.
- Preferred validation command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`.

## Testing Notes

- Existing automation memory indicates prior coverage work used focused JUnit 4/Mockito tests and XML mapper parsing tests.
- Avoid database-dependent tests unless a local test database/fixture is explicitly available.

## Credentials

- No credential values are stored here. Record only credential locations if they become relevant.
