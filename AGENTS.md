# Project Agent Instructions

- Project: Hospital
- Last updated: 2026-07-17
- Stack inferred from repository: Java, Maven, Spring Boot 2.0.0.RELEASE, MyBatis, Freemarker, MySQL, Layui.

## Working Rules

- Use Chinese for user-facing explanations unless the user requests otherwise.
- Prefer small, high-confidence fixes that match existing Spring MVC/MyBatis patterns.
- For bug-finding automation, only change code when there is a concrete high-severity trigger scenario.
- Run focused tests or builds when dependencies are available; record environment blockers instead of hiding them.
- Do not store secrets in project memory. Record credential locations only when needed.

## Repository Notes

- The repo may target an older Java/Spring Boot/Lombok stack. Check build compatibility before assuming test failures indicate product regressions.
