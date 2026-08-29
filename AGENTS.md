# Hospital Agent Guide

Project: Hospital
Date initialized: 2026-08-29

## Confirmed stack

- Java Spring Boot 2.0.0 application.
- Maven project (`pom.xml`).
- MyBatis mapper XML under `src/main/resources/mapper`.
- Freemarker templates under `src/main/resources/templates`.
- Layui/jQuery static assets under `src/main/resources/static`.
- MySQL datasource configured in `src/main/resources/application.yml`.

## Working rules

- Follow the repository's existing MVC layout: controllers in `controller`, services in `service`/`service.impl`, DAOs in `dao`, entities in `entity`, mapper XML in `resources/mapper`.
- Keep high-severity bug fixes minimal and covered by focused tests where practical.
- Do not record credential values in project memory; record only their file locations when relevant.
- Before changing behavior, trace controller, service, mapper, and template callers to confirm the user-facing impact.

## Validation notes

- Prefer `mvn test` for automated validation.
- This repository may need Maven installed in the cloud image before tests can run.
