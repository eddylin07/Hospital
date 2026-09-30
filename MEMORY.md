# MEMORY.md

## Project Snapshot

- Project name: Hospital
- Created: 2026-09-30
- Inferred stack: Java, Maven, Spring Boot, MyBatis, Freemarker, MySQL

## Operational Notes

- `AGENTS.md` and `MEMORY.md` were absent on 2026-09-30; template files were not present under `~/.codex/templates/`, so minimal project files were created from inferred repository context.
- Prescription dispensing writes through `SeekMapper.updateDrugs`; it must update only the patient's latest `seek` row (`ORDER BY id DESC LIMIT 1`) so historical visits are not overwritten.
- Do not store credentials or secret values here. Record only locations and durable project knowledge.
