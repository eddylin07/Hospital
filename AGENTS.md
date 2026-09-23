# Hospital agent notes

Project: Hospital
Date initialized: 2026-09-23

## Inferred stack

- Java Spring Boot 2.0.0.RELEASE web application.
- Maven project (`pom.xml`).
- MyBatis with mapper XML files under `src/main/resources/mapper`.
- FreeMarker templates under `src/main/resources/templates`.
- MySQL schema/data under `sql/hospital.sql`.

## Working rules

- Keep fixes minimal and focused; avoid broad refactors during critical bug investigations.
- Prefer server-side authorization and data integrity checks over template/UI-only controls.
- When changing behavior, add or update focused tests when feasible and run the relevant Maven test command.
- Do not store secrets or credential values in this file.

## Known environment notes

- The repository may need Maven installed in the cloud image before tests can run.
- Older dependencies may require compatibility updates when running tests on modern JDKs.
