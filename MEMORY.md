# Project Memory

Last initialized: 2026-08-12

Project facts:
- Hospital is a Java/Maven hospital information management system using Spring Boot 2.0, MyBatis, Freemarker templates, and MySQL.
- The repository currently uses branch `cursor/critical-bug-investigation-b52d` for this automation run.

Operational notes:
- Always read this file and `AGENTS.md` before changing project files.
- Critical bug work should prioritize concrete triggers with high impact: data loss, crashes in important flows, authorization bypasses, race conditions, or silent corruption.
- Validate fixes with focused automated tests when possible; run `mvn test` when Maven is available.
- If Maven is missing in the cloud image, install Maven with the system package manager before running tests.

Open questions:
- None for the current repository setup.

