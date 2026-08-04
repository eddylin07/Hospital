# AGENTS.md

## Project

- Name: Hospital
- Stack: Java, Spring Boot, Maven, MyBatis, FreeMarker templates, MySQL SQL seed/schema files.
- Date initialized: 2026-08-04

## Working guidelines

- Default communication language: Chinese.
- Keep fixes minimal and aligned with existing Spring MVC/MyBatis service patterns.
- For bug-finding automation, only change code when a concrete high-severity trigger is identified.
- Do not store credentials or secret values in this repository. If a credential location matters, record only the location.

## Testing

- Prefer focused unit tests for narrow service/controller/interceptor fixes.
- Run `mvn test` when Maven is available. If Maven is missing in the cloud image, install it with `sudo apt-get update && sudo apt-get install -y maven`.
- This project may need Java 21 compatibility fixes for Lombok/Surefire before tests can run reliably.

## Git

- Work on the current automation branch unless the user explicitly instructs otherwise.
- Commit and push code changes to the configured branch before opening a PR.
