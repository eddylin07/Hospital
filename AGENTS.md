# AGENTS.md

## Project

- Name: Hospital
- Type: Java web application
- Stack: Spring Boot 2.0.0, MyBatis, Freemarker, Maven, MySQL
- Date initialized: 2026-08-08

## Working rules

- Follow repository-local patterns before introducing new abstractions.
- Keep bug-fix changes minimal and focused.
- Prefer server-side authorization and data-integrity checks for medical workflow behavior.
- Do not commit secrets or credential values; document only where they are configured.

## Testing

- Use Maven for Java tests: `mvn test`.
- This cloud image may not include Maven by default; install it with apt if needed.
- Spring Boot 2.0 era test dependencies may be incompatible with Java 21, so prefer simple fakes over Mockito unless dependencies are upgraded.

## Unknown / to be completed

- Production deployment target: unknown.
- Required Java runtime in production: unknown.
- Database migration process: unknown.
