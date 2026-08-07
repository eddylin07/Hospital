# AGENTS.md

## Project

- Name: Hospital
- Stack: Java, Spring Boot, Maven, JSP/static frontend assets, MyBatis-style mapper layer
- Default branch: master

## Working Guidelines

- Follow repository patterns and keep bug-fix changes minimal.
- For critical-bug automation, inspect recent commits and trace full request/service/mapper paths before deciding to change code.
- Only open a PR when there is a concrete high-severity trigger and a high-confidence fix.
- Prefer focused regression tests for security, data-loss, crash, or corruption fixes.

## Validation

- Use Maven tests when available: `mvn test`.
- This cloud image may not include Maven; install it with apt if `mvn` is missing.
- Java 21 compatibility may require updated Lombok/Surefire versions if the branch still has old dependency versions.
