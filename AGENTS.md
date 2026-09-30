# Project Guide

## Project

- Name: Hospital
- Purpose: hospital/medical information management system.
- Stack: Java, Spring Boot 2.0, Maven, MyBatis, FreeMarker, MySQL.

## Repository Conventions

- Main application entry point: `src/main/java/com/hospital/HospitalApp.java`.
- Production Java code lives under `src/main/java/com/hospital`.
- Tests should live under `src/test/java` and follow the existing Maven/Spring Boot test setup.
- Keep tests deterministic and independent; prefer focused unit tests for utilities and controller/service edge cases when database state is not required.

## Common Commands

- Compile: `mvn test-compile`
- Run tests: `mvn test`
- Run a focused test: `mvn -Dtest=ClassName test`

## Notes for Agents

- Read `MEMORY.md` before modifying code.
- Do not add credentials to repository files or memory; record only their locations if needed.
- Follow existing package names and naming style, including the current `uitls` package spelling.
