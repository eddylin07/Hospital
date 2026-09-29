# Agent Guide

## Project

- Name: Hospital
- Repository root: `/workspace`
- Created from local inference on 2026-09-29 because no template files were available under `~/.codex/templates/`.

## Stack

- Java Maven project
- Spring Boot 2.0.0.RELEASE
- MyBatis / PageHelper
- FreeMarker templates
- MySQL connector
- Test dependency: `spring-boot-starter-test`

## Working rules

- Read `MEMORY.md` before changing code.
- Follow existing Maven/Spring conventions and keep changes narrowly scoped.
- Prefer deterministic unit or slice tests over environment-dependent integration tests unless database/application context setup is already stable.
- Validate touched areas with the most specific Maven test target available.

## Open items to confirm with maintainers

- Supported JDK version.
- Required local database setup for full integration tests.
- Preferred test naming/package conventions if they differ from Maven defaults.
