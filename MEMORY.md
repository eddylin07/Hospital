# Project Memory

## Project Facts
- Project name: Hospital.
- Detected on 2026-10-16.
- Stack: Maven Java project using Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL connector, and Spring Boot test support.
- Source layout follows standard Maven paths under `src/main/java` and `src/main/resources`.

## Testing Notes
- Prefer tests that avoid a real database unless a local fixture is already present.
- Start with focused unit tests for utility or controller logic when recent changes touch high-risk behavior.

## Open Questions
- None yet.
