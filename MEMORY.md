# MEMORY.md

## Project memory

- Project: Hospital.
- Inferred stack on 2026-07-18: Java Spring Boot 2.0.0.RELEASE, Maven, MyBatis, FreeMarker, MySQL.
- Local templates expected at `/home/ubuntu/.codex/templates/` were not present during setup, so `AGENTS.md` and this file were created from project inference.

## Architecture notes

- Spring Boot entry point is `src/main/java/com/hospital/HospitalApp.java`.
- Data access uses MyBatis mapper interfaces in `src/main/java/com/hospital/dao/` and XML mappers in `src/main/resources/mapper/`.
- UI templates are FreeMarker `.flt` files in `src/main/resources/templates/`.

## Operational notes

- Record new durable findings here when they affect future work.
- Do not store secret values in this file.
