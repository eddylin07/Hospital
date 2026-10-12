# MEMORY.md

## Project facts

- Project name: Hospital.
- Initialized from automation on 2026-10-12 because no project memory file existed.
- Inferred stack: Java Maven application using Spring Boot 2.0.0, MyBatis, Freemarker templates, and MySQL.

## Testing notes

- Use Maven test targets for Java validation.
- Prefer focused tests that avoid requiring a live database unless the existing test pattern already provisions one.
- `SeekMapperXmlTest` parses mapper XML with MyBatis directly, avoiding a live MySQL dependency while covering generated prescription SQL.
- Validation passed with `JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn test` on 2026-10-12.

## Open confirmations

- No template files were found under `~/.codex/templates/`; these files were initialized with inferred minimal content.
- Confirm whether there are project-specific commands or fixtures that should be added here.
