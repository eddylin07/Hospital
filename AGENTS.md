# AGENTS.md

## Project

- Name: Hospital
- Type: Java Spring Boot medical information management system
- Build tool: Maven
- Main application: `com.hospital.HospitalApp`

## Working guidelines

- Follow the repository's existing package and naming conventions.
- Prefer focused changes with matching tests for behavior that affects users or business logic.
- Do not commit credentials. Record credential locations only, never values.

## Testing

- Use Maven for Java tests.
- Prefer narrow test targets for touched classes when possible, then broaden if the change affects shared behavior.

## Pending user/project details

- Deployment environment: TBD
- Required database/runtime services for full integration tests: TBD
