# Agent Instructions

Project: Hospital

Stack:
- Java Spring Boot 2.0.0.RELEASE
- Maven
- MyBatis
- Freemarker templates
- MySQL connector

Working rules:
- Read `AGENTS.md` and `MEMORY.md` before making project changes.
- Keep changes narrow and aligned with the existing Spring MVC/MyBatis style.
- For critical bug investigations, trace full request/service/mapper paths before changing code.
- Add focused tests for any behavioral fix when practical.
- Prefer `mvn test` for validation. If Maven is unavailable in the cloud image, install it before testing.
- Do not store credentials or secret values in repository memory; record only where they are configured.

Branch:
- Development branch for this automation run: `cursor/critical-bug-investigation-b52d`.

