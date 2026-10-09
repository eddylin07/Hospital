# Agent Instructions

Project: Hospital

Detected stack:
- Java Maven application
- Spring Boot 2.0.0.RELEASE
- MyBatis / PageHelper
- Freemarker templates
- MySQL connector
- JUnit/Spring Boot test support via `spring-boot-starter-test`

Working guidelines:
- Follow existing Java package and test conventions.
- Prefer focused regression tests over broad snapshot-style tests.
- Keep tests deterministic and independent of external services where possible.
- Run relevant Maven test targets before committing test changes.

