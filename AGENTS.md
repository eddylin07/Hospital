# AGENTS.md

## Project

- Name: Hospital
- Type: Java web application
- Stack inferred on 2026-10-05: Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL.

## Working rules

- Prefer minimal, high-confidence changes that match the existing Spring MVC/MyBatis structure.
- For bug-fix work, trace the caller path before editing and keep the fix scoped to the confirmed issue.
- Run the most relevant Maven checks available before committing code changes.
- Do not commit credentials or local environment values.

## Notes

- Repository-specific conventions are still being discovered. Update this file when durable workflow rules are learned.
