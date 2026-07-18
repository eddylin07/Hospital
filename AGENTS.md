# AGENTS.md

## Project

- Name: Hospital
- Root: `/workspace`
- Purpose: Hospital information management system.
- Primary stack: Java, Spring Boot 2.0.0.RELEASE, Maven, MyBatis, FreeMarker, MySQL.
- Created from local inference on 2026-07-18 because `/home/ubuntu/.codex/templates/` was unavailable in this environment.

## Working rules

- Read this file and `MEMORY.md` before changing project files.
- Keep changes minimal and aligned with the existing Spring MVC/MyBatis structure.
- Prefer focused fixes over broad refactors.
- Use Maven for build and test validation when dependencies are available.
- Do not record credential values; record only credential locations when needed.

## Repository notes

- Main application entry point: `src/main/java/com/hospital/HospitalApp.java`.
- Configuration: `src/main/resources/application.yml`.
- MyBatis mapper XML files: `src/main/resources/mapper/`.
- FreeMarker templates use the `.flt` extension under `src/main/resources/templates/`.
