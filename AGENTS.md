# Project Agents Guide

## Project

- Name: Hospital
- Stack: Java, Maven, Spring Boot, MyBatis, Freemarker templates, MySQL SQL assets.
- Primary source: `src/main/java/com/hospital`
- Templates/static assets: `src/main/resources`

## Working rules

- Read `MEMORY.md` before changing code.
- Keep changes minimal and focused on the requested behavior.
- Prefer existing controller/service/mapper patterns over introducing new abstractions.
- For bug fixes, trace the request path from controller to service/mapper and validate with the narrowest reliable test or build command available.
- Do not record secrets in repository docs; record only the location or configuration key.

## Validation

- Use Maven commands from the repository root when validating Java changes.
- If the project cannot run tests due to missing external services or dependencies, record the exact command and failure.
