# AGENTS.md

## Project

- Name: Hospital
- Type: Spring Boot 2 / MyBatis hospital management application
- Primary language: Java
- Build tool: Maven

## Working guidelines

- Default communication language: Chinese.
- Prefer focused changes that follow the existing package and test conventions.
- Do not change production behavior when adding coverage unless a small fix is required to make the intended invariant true.
- Keep tests deterministic and independent.

## Validation

- Use JDK 8 for Maven in this repository because the checked-in Lombok version does not compile on modern JDKs.
- Preferred full validation command:

```bash
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test
```

- If Maven or JDK 8 is missing in a fresh environment, install them before validation.
