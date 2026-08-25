# Agent Instructions

## Project

- Project name: Hospital
- Stack: Spring Boot 2.0, MyBatis, Freemarker, Maven, JUnit 4/Spring Boot test.
- Domain: hospital / medical information management system.

## Development Rules

- Work on the branch assigned by the cloud task.
- Keep production changes minimal; prefer adding focused regression tests.
- Follow existing Java package structure under `com.hospital`.
- Do not record credential values in documentation or memory. If credentials are relevant, record only the file path that contains them.

## Testing

- Use JDK 8 for Maven validation because the checked-in Lombok version is not compatible with modern javac module access.
- Preferred validation command:

```sh
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If Maven or JDK 8 are missing in a fresh cloud image, install them before validation.
