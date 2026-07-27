# AGENTS.md

## Project

- Name: Hospital
- Type: Medical information management system
- Stack: Java, Maven, Spring Boot 2.0, MyBatis, FreeMarker, MySQL

## Working rules

- Read `MEMORY.md` before changing code.
- Keep fixes minimal and focused on concrete correctness, security, data-loss, or crash risks.
- Prefer existing controller/service/mapper patterns over broad refactors.
- Add focused tests when changing behavior.
- Do not record credential values in repository files; record only locations or setup requirements.

## Validation

- Use `mvn test` when Maven is available.
- If Maven is missing in the cloud image, install it with `sudo apt-get update && sudo apt-get install -y maven` before relying on tests.
