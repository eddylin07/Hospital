# AGENTS.md

## Project

- Name: Hospital
- Repository root: `/workspace`
- Stack: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, FreeMarker, Layui
- Artifact: `com.wxthxy.hospital:HospitalAction`

## Working Rules

- Default communication language: Chinese.
- Prefer existing Spring Boot/MyBatis/JUnit conventions in this repository.
- Keep test additions focused on meaningful regression risk.
- Do not change production behavior unless a small testability refactor is required.
- For this repository, use Java 8 when running Maven because Lombok 1.16.22 is not compatible with modern javac module access defaults.

## Validation

- Preferred test command: `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test`
- If JDK 8 or Maven is missing in the environment, install or configure them before validation.

