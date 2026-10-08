# AGENTS.md

## Project

- Name: Hospital
- Stack: Java Spring Boot 2.0, Maven, MyBatis, Freemarker, MySQL.
- Main application: `src/main/java/com/hospital/HospitalApp.java`.

## Development notes

- Prefer small, focused fixes for security and data-integrity issues.
- Role ids used by the application: admin=1, doctor=2, patient=3.
- Do not record credential values. `src/main/resources/application.yml` may contain local database configuration.

## Testing

- Run `mvn test` for Java changes when Maven is available.
- This repository may require Maven installation in a fresh cloud image.
